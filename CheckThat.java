package tests.util;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.function.Executable;

public class CheckThat {
	public interface TypeCondition {}

	public static enum Staticness implements TypeCondition { STATIC, NOT_STATIC }
	public static enum Modifiability implements TypeCondition { MODIFIABLE, NOT_MODIFIABLE }
	public static enum Abstractness implements TypeCondition { ABSTRACT, NOT_ABSTRACT }

	public enum Visibility implements TypeCondition {
		PUBLIC(Modifier::isPublic, "publikus", "public"),
		PACKAGE_PRIVATE(mod -> Stream.<Predicate<Integer>>of(Modifier::isPublic, Modifier::isProtected, Modifier::isPrivate).noneMatch(cond -> cond.test(mod)), "félnyilvános", "package private"),
		PROTECTED(Modifier::isProtected, "protected", "protected"),
		PRIVATE(Modifier::isPrivate, "privát", "private");

		public Predicate<Integer> condition;
		public String huName;
		public String enName;

		Visibility(Predicate<Integer> condition, String huName, String enName) {
			this.condition = condition;
			this.huName = huName;
			this.enName = enName;
		}

		public static Visibility getVisibility(int mod) {
			return Arrays.stream(values()).filter(v -> v.condition.test(mod)).findFirst().get();
		}
	}

	private enum ClassType { A_CLASS, AN_INTERFACE, AN_ENUM }
	private enum InspectedMember { CONSTRUCTOR, METHOD, FIELD }

	String className;
	Class<?> clazz;
	Class<?> parentClass;
	ClassType classType;

	InspectedMember inspectedMember;
	boolean isInspectedMemberStatic;
	Visibility inspectedMemberVisibility;
	boolean isInspectedMemberModifiable;
	boolean isInspectedMemberAbstract;
	String signature;
	Class<?> type;
	Class<?>[] exceptionTypes;

	public CheckThat(String className, ClassType classType) {
		this(className, classType, Optional.empty());
	}

	public CheckThat(String className, ClassType classType, Optional<Consumer<CheckThat>> extraCheck) {
		try {
			clazz = Class.forName(className);

			extraCheck.ifPresent(consumer -> consumer.accept(this));

			inspectedMemberVisibility = Visibility.getVisibility(clazz.getModifiers());
			isInspectedMemberModifiable = !Modifier.isFinal(clazz.getModifiers());
			isInspectedMemberAbstract = Modifier.isAbstract(clazz.getModifiers());
			isInspectedMemberStatic = Modifier.isStatic(clazz.getModifiers());
		} catch (Exception e) {
			fail(() -> String.format("A %s %s nem létezik", className, classType == ClassType.AN_ENUM ? "enum" : "osztály"));
		}

		if (classType == ClassType.AN_INTERFACE) {
			assertTrue(clazz.isInterface(), () -> String.format("A %s nem interfész", className));
		}

		if (classType == ClassType.AN_ENUM) {
			assertTrue(clazz.isEnum(), () -> String.format("A %s nem felsorolási típus", className));

			assertAll(
				Arrays.stream((Enum<?>[])clazz.getEnumConstants())
					.map(Enum::name)
					.map(enumConstantName ->
						(Executable)() -> assertTrue(enumConstantName.matches("[A-Z0-9_]+"),
							() -> String.format("A %s felsorolási típus '%s' eleme nem csupa nagybetűs", className, enumConstantName)))
					.collect(Collectors.toList())
			);
		}

		this.className = className;
		this.classType = classType;
	}

	public static CheckThat theClassWithParent(String name, String parentName) throws Exception {
		Consumer<CheckThat> parentCheck = checkThat -> {
			try {
				checkThat.parentClass = Class.forName(parentName);
				assertEquals(checkThat.parentClass, checkThat.clazz.getSuperclass(),
					() -> String.format("A %s osztály nem szülője a %s osztálynak", parentName, checkThat.className));
			} catch (Exception e) {
				fail(() -> String.format("A %s szülőosztály nem létezik", parentName));
			}
		};
		return new CheckThat(name, ClassType.A_CLASS, Optional.of(parentCheck));
	}

	public static CheckThat theCheckedException(String name) throws Exception {
		Consumer<CheckThat> exceptionCheck = checkThat -> {
			assertEquals(Exception.class, checkThat.clazz.getSuperclass(),
				() -> String.format("A %s nem ellenőrzött kivétel", checkThat.className));
		};
		return new CheckThat(name, ClassType.A_CLASS, Optional.of(exceptionCheck));
	}

	public static CheckThat theUncheckedException(String name) throws Exception {
		Consumer<CheckThat> exceptionCheck = checkThat -> {
			assertEquals(RuntimeException.class, checkThat.clazz.getSuperclass(),
				() -> String.format("A %s nem nem-ellenőrzött kivétel", checkThat.className));
		};
		return new CheckThat(name, ClassType.A_CLASS, Optional.of(exceptionCheck));
	}

	public static CheckThat theClass(String name) throws Exception {
		return new CheckThat(name, ClassType.A_CLASS);
	}

	public static CheckThat theInterface(String name) throws Exception {
		return new CheckThat(name, ClassType.AN_INTERFACE);
	}

	public static CheckThat theEnum(String name) throws Exception {
		return new CheckThat(name, ClassType.AN_ENUM);
	}

	private static String simpleParamText(Class<?>... expectedParams) {
		return Arrays.stream(expectedParams).map(Class::getSimpleName).collect(Collectors.joining(","));
	}

	private String methodSignature(String methodName, Class<?>... expectedParams) {
		return String.format("%s.%s(%s)", clazz.getSimpleName(), methodName, simpleParamText(expectedParams));
	}

	public CheckThat hasConstructorWithParams(String... expectedParams) throws Exception {
		return hasConstructorWithParams(Arrays.stream(expectedParams).map(noExc(CheckThat::getType)).toArray(Class[]::new));
	}

	public CheckThat hasConstructorWithParams(Class<?>... expectedParams) throws Exception {
		String errMsg = String.format("A %s osztály nem rendelkezik (%s) paraméterezésű konstruktorral", className, simpleParamText(expectedParams));
		try {
			Constructor<?> constructor = clazz.getDeclaredConstructor(expectedParams);
			assertNotNull(constructor, errMsg);

			inspectedMember = InspectedMember.METHOD;
			inspectedMemberVisibility = Visibility.getVisibility(constructor.getModifiers());
			isInspectedMemberAbstract = Modifier.isAbstract(constructor.getModifiers());
		} catch (NoSuchMethodException e) {
			fail(errMsg);
		}
		return this;
	}

	public CheckThat hasMethodWithNoParams(String name) throws Exception {
		return hasMethodWithParams(name, new Class[0]);
	}

	public CheckThat hasMethodWithParams(String name, Class<?>... expectedParams) throws Exception {
		signature = methodSignature(name, expectedParams);
		try {
			Method method = clazz.getDeclaredMethod(name, expectedParams);

			type = method.getReturnType();
			inspectedMember = InspectedMember.METHOD;
			isInspectedMemberStatic = Modifier.isStatic(method.getModifiers());
			inspectedMemberVisibility = Visibility.getVisibility(method.getModifiers());
			isInspectedMemberModifiable = !Modifier.isFinal(method.getModifiers());
			isInspectedMemberAbstract = Modifier.isAbstract(method.getModifiers());

			exceptionTypes = method.getExceptionTypes();
		} catch (NoSuchMethodException e) {
			fail(() -> String.format("A(z) %s osztály nem rendelkezik %s metódussal", clazz.getSimpleName(), signature));
		}

		return this;
	}

	public CheckThat hasMethodWithParams(String name, String... expectedParamTypeNames) throws Exception {
		Class<?>[] expectedParams = new Class[expectedParamTypeNames.length];
		for (int i = 0; i < expectedParamTypeNames.length; i++) {
			expectedParams[i] = getType(expectedParamTypeNames[i]);
		}
		return hasMethodWithParams(name, expectedParams);
	}

	public CheckThat hasFieldOfType(String name, String typeName) throws Exception {
		return hasFieldOfType(name, getType(typeName));
	}

	public CheckThat hasFieldOfType(String name, Class<?> type) throws Exception {
		signature = String.format("%s.%s", clazz.getSimpleName(), name);
		try {
			Field field = clazz.getDeclaredField(name);
			assertEquals(type, field.getType(), () -> String.format("A %s adattag típusa nem %s", signature, type.getSimpleName()));

			this.type = field.getType();
			inspectedMember = InspectedMember.FIELD;
			isInspectedMemberStatic = Modifier.isStatic(field.getModifiers());
			inspectedMemberVisibility = Visibility.getVisibility(field.getModifiers());
			isInspectedMemberModifiable = !Modifier.isFinal(field.getModifiers());
			isInspectedMemberAbstract = Modifier.isAbstract(field.getModifiers());
		} catch (NoSuchFieldException e) {
			fail(() -> String.format("Az osztály nem rendelkezik %s adattaggal", signature));
		}

		return this;
	}

	public CheckThat hasEnumElements(String... elems) {
		var enumConstants = Arrays.stream(clazz.getEnumConstants()).map(Object::toString).collect(Collectors.toSet());

		assertEquals(enumConstants.size(), elems.length,
			() -> String.format("A %s enum elemszáma nem megfelelő", className));

		for (String elem : elems) {
			assertTrue(enumConstants.contains(elem),
				() -> String.format("A %s enum nem tartalmazza ezt az elemet: %s", className, elem));
		}
		return this;
	}

	public CheckThat thatIs(TypeCondition... conds) {
		assertAll(Arrays.stream(conds).map(cond -> getTypeCondition(cond)).toList());
		return this;
	}

	private Executable getTypeCondition(TypeCondition cond) {
		if (cond instanceof Staticness statix) {
			if (inspectedMember == InspectedMember.CONSTRUCTOR)  return () -> fail("Invalid check: constructors should not be checked for staticness");
			return () -> assertEquals(statix, isInspectedMemberStatic ? Staticness.STATIC : Staticness.NOT_STATIC);
		}

		if (cond instanceof Visibility visibility) {
			return () -> assertEquals(visibility, inspectedMemberVisibility);
		}

		if (cond instanceof Modifiability modifiability) {
			if (inspectedMember != InspectedMember.FIELD)  return () -> fail("Invalid check: modifiability should only be checked for fields");
			return () -> assertEquals(modifiability == Modifiability.MODIFIABLE, isInspectedMemberModifiable);
		}

		if (cond instanceof Abstractness abstractness) {
			return () -> assertEquals(abstractness == Abstractness.ABSTRACT, isInspectedMemberAbstract);
		}
		return () -> fail("Invalid type condition");
	}

	public CheckThat thatReturns(Class<?> expectedReturnType) {
		assertEquals(expectedReturnType, type);
		return this;
	}

	public CheckThat thatReturns(String expectedReturnTypeName) throws ClassNotFoundException {
		Class<?> expectedReturnType = getType(expectedReturnTypeName);
		return thatReturns(expectedReturnType);
	}

	public CheckThat thatHasParentClass(Class<?> expectedSuperType) {
		assertEquals(expectedSuperType, clazz.getSuperclass(),
			() -> String.format("A %s osztály szülője nem %s", className, expectedSuperType.getSimpleName()));
		return this;
	}

	public CheckThat thatHasParentClass(String expectedSuperTypeName) throws Exception {
		return thatHasParentClass(getType(expectedSuperTypeName));
	}

	public CheckThat thatThrows(String expectedExceptionTypeName) throws Exception {
		Class<?> et = getType(expectedExceptionTypeName);
		return thatThrows(et);
	}

	public CheckThat thatThrows(Class<?> expectedExceptionType) throws Exception {
		boolean isUncheckedException = RuntimeException.class.isAssignableFrom(expectedExceptionType);
		if (!isUncheckedException) {
			assertTrue(Arrays.stream(exceptionTypes).anyMatch(et -> et == expectedExceptionType),
				() -> String.format("A %s metódus nem válthat ki %s kivételt", signature, expectedExceptionType.getSimpleName()));
		}
		return this;
	}

	public CheckThat thatReturnsNothing() {
		assertEquals(void.class, type,
			() -> String.format("A %s metódusnak nem lehet visszatérési értéke", signature));
		return this;
	}

	// -----------------------------------------------------------

	static Map<String, Class<?>> primitivesAndVoid = Map.of(
		"void", void.class,
		"boolean", boolean.class,
		"byte", byte.class,
		"short", short.class,
		"char", char.class,
		"int", int.class,
		"long", long.class,
		"float", float.class,
		"double", double.class
	);

	private static Class<?> getType(String typeName) throws ClassNotFoundException {
		try {
			return Class.forName(typeName);
		} catch (ClassNotFoundException e) {
			if (primitivesAndVoid.containsKey(typeName))   return primitivesAndVoid.get(typeName);

			throw e;
		}
	}

	private static interface NoExcFunction<T, U> {
		U apply(T par) throws Exception;
	}

	private static <T, U> Function<T, U> noExc(NoExcFunction<T, U> fun) {
		return par -> {
			try {
				return fun.apply(par);
			} catch (Exception e) {
				fail(() -> e.toString());
				return null;
			}
		};
	}
}
