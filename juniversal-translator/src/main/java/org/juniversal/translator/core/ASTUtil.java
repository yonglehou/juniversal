/*
 * Copyright (c) 2012-2014, Microsoft Mobile
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.juniversal.translator.core;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ASTUtil {

    public static void parseJava(List<File> javaProjectDirectories) {

		/*
        ArrayList<File> files = new ArrayList<File>();
		for (File javaProjectDirectory : javaProjectDirectories)
			Util.getFilesRecursive(javaProjectDirectory, ".java", files);
	
		ArrayList<String> filePathsList = new ArrayList<String>();
		for (File file : files) {
			filePathsList.add(file.getPath());
		}

		String[] filePaths = filePathsList.toArray(new String[filePathsList.size()]);

		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		parser.createASTs(filePaths, encodings, bindingKeys, requestor, monitor)

		String source = readFile(args[0]);
		parser.setSource(source.toCharArray());

		CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null /xx* IProgressMonitor *xx/);

		TypeDeclaration typeDeclaration = ASTUtil.getFirstTypeDeclaration(compilationUnit);

		StringWriter writer = new StringWriter();
		CPPProfile profile = new CPPProfile();
		// profile.setTabStop(4);

		CPPWriter cppWriter = new CPPWriter(writer, profile);

		Context context = new Context((CompilationUnit) compilationUnit.getRoot(), source, 8, profile, cppWriter,
				OutputType.SOURCE);

		context.setPosition(typeDeclaration.getStartPosition());

		ASTWriters astWriters = new ASTWriters();

		try {
			context.setPosition(typeDeclaration.getStartPosition());
			skipSpaceAndComments();

			astWriters.writeNode(typeDeclaration, context);
		} catch (UserViewableException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		} catch (RuntimeException e) {
			if (e instanceof ContextPositionMismatchException)
				throw e;
			else
				throw new JUniversalException(e.getMessage() + "\nError occurred with context at position\n"
						+ context.getPositionDescription(context.getPosition()), e);
		}

		String cppOutput = writer.getBuffer().toString();

		System.out.println("Output:");
		System.out.println(cppOutput);
		*/

    }

    public static TypeDeclaration getFirstTypeDeclaration(CompilationUnit compilationUnit) {
        return (TypeDeclaration) compilationUnit.types().get(0);
    }

    public static Block getFirstMethodBlock(CompilationUnit compilationUnit) {
        return getFirstTypeDeclaration(compilationUnit).getMethods()[0].getBody();
    }

    /**
     * Return true is the specified modifier is an @Nullable annotation.   Currently we don't care about, nor even
     * support, fully qualified names for the @Nullable annotation.   Any non-qualified name that's @Nullable counts
     * here, but fully names currently never do currently.   The intention is that the programmer can pick between
     * several different @Nullable annotations, though JSimple code currently uses the IntelliJ one.
     *
     * @param extendedModifier modifier in question
     * @return true if modifier is an annotation for @Nullable, false otherwise
     */
    public static boolean isNullable(IExtendedModifier extendedModifier) {
        if (!(extendedModifier instanceof MarkerAnnotation))
            return false;

        Name typeName = ((MarkerAnnotation) extendedModifier).getTypeName();
        return typeName.isSimpleName() && ((SimpleName) typeName).getIdentifier().equals("Nullable");
    }

    /**
     * Return true is the specified modifier is "final".
     *
     * @param extendedModifier modifier in question
     * @return true if modifier is "final"
     */
    public static boolean isFinal(IExtendedModifier extendedModifier) {
        return extendedModifier instanceof Modifier && ((Modifier) extendedModifier).isFinal();
    }

    /**
     * Return true is the specified modifier is "final".
     *
     * @param extendedModifier modifier in question
     * @return true if modifier is "final"
     */
    public static boolean isAnnotation(IExtendedModifier extendedModifier) {
        return extendedModifier instanceof Annotation;
    }

    /**
     * Returns true if the list of extended modifiers (modifiers & annotations) includes "final".
     *
     * @param extendedModifiers extended modifiers to check
     * @return true if and only if list contains "final"
     */
    public static boolean containsFinal(List<?> extendedModifiers) {
        for (Object extendedModifierObject : extendedModifiers) {
            if (isFinal((IExtendedModifier) extendedModifierObject))
                return true;
        }
        return false;
    }

    /**
     * Returns true if the list of extended modifiers (modifiers & annotations) includes "static".
     *
     * @param extendedModifiers extended modifiers to check
     * @return true if and only if list contains "static"
     */
    public static boolean containsStatic(List<?> extendedModifiers) {
        for (Object extendedModifierObject : extendedModifiers) {
            IExtendedModifier extendedModifier = (IExtendedModifier) extendedModifierObject;
            if (extendedModifier.isModifier() && ((Modifier) extendedModifier).isStatic())
                return true;
        }
        return false;
    }

    /**
     * Returns true if the list of extended modifiers (modifiers & annotations) includes "abstract".
     *
     * @param extendedModifiers extended modifiers to check
     * @return true if and only if list contains "static"
     */
    public static boolean containsAbstract(List<?> extendedModifiers) {
        for (Object extendedModifierObject : extendedModifiers) {
            IExtendedModifier extendedModifier = (IExtendedModifier) extendedModifierObject;
            if (extendedModifier.isModifier() && ((Modifier) extendedModifier).isAbstract())
                return true;
        }
        return false;
    }

    /**
     * Determines the access modifier specified in list of modifiers. If no access modifier is specified, the default
     * access of Package is returned.
     *
     * @param extendedModifiers extended modifiers to check
     * @return AccessModifier specified or Package by default
     */
    public static AccessLevel getAccessModifier(List<?> extendedModifiers) {
        for (Object extendedModifierObject : extendedModifiers) {
            IExtendedModifier extendedModifier = (IExtendedModifier) extendedModifierObject;
            if (extendedModifier.isModifier()) {
                Modifier modifier = (Modifier) extendedModifier;
                if (modifier.isPublic())
                    return AccessLevel.PUBLIC;
                else if (modifier.isProtected())
                    return AccessLevel.PROTECTED;
                else if (modifier.isPrivate())
                    return AccessLevel.PRIVATE;
            }
        }

        return AccessLevel.PACKAGE;
    }

    public static boolean isFinal(TypeDeclaration typeDeclaration) {
        return containsFinal(typeDeclaration.modifiers());
    }

    public static boolean isFinal(MethodDeclaration methodDeclaration) {
        return containsFinal(methodDeclaration.modifiers());
    }

    public static boolean isStatic(MethodDeclaration methodDeclaration) {
        return containsStatic(methodDeclaration.modifiers());
    }

    public static boolean isPrivate(MethodDeclaration methodDeclaration) {
        return getAccessModifier(methodDeclaration.modifiers()) == AccessLevel.PRIVATE;
    }

    public static boolean isOverride(MethodDeclaration methodDeclaration) {
        for (Object extendedModifierObject : methodDeclaration.modifiers()) {
            IExtendedModifier extendedModifier = (IExtendedModifier) extendedModifierObject;
            if (extendedModifier.isAnnotation()) {
                // TODO: Check for full, resolved binding annotation type name, matching against java.lang.Override as
                // soon as figure & fix out why calling resolveAnnotationBinding returned null here
                Annotation annotation = (Annotation) extendedModifier;
                if (annotation.getTypeName().getFullyQualifiedName().equals("Override"))
                    return true;
            }
        }

        return false;
    }

    public static boolean isConstructor(MethodDeclaration methodDeclaration) {
        return methodDeclaration.isConstructor();
    }

    public static boolean isStatic(IMethodBinding methodBinding) {
        return (methodBinding.getModifiers() & Modifier.STATIC) != 0;
    }

    public static boolean isType(Type type, String qualifiedTypeName) {
        return isType(type.resolveBinding(), qualifiedTypeName);
    }

    public static boolean isType(@Nullable ITypeBinding typeBinding, String qualifiedTypeName) {
        return typeBinding != null && typeBinding.getQualifiedName().equals(qualifiedTypeName);
    }

    public static boolean isMethod(MethodDeclaration methodDeclaration, String methodName, String... parameterTypes) {
        // If the method names don't match, it's not the specified method
        if (!methodDeclaration.getName().getIdentifier().equals(methodName))
            return false;

        // If the method parameter counts don't match, it's not the specified method
        if (methodDeclaration.parameters().size() != parameterTypes.length)
            return false;

        // Ensure each parameter has the expected type
        int index = 0;
        for (Object parameterObject : methodDeclaration.parameters()) {
            SingleVariableDeclaration parameter = (SingleVariableDeclaration) parameterObject;

            if (!isType(parameter.getType(), parameterTypes[index]))
                return false;

            ++index;
        }

        return true;
    }

    public static boolean isArrayLengthField(FieldAccess fieldAccess) {
        if (!fieldAccess.getName().getIdentifier().equals("length"))
            return false;
        @Nullable IVariableBinding variableBinding = fieldAccess.resolveFieldBinding();
        return variableBinding != null && variableBinding.getType().isArray();
    }

    public static boolean isArrayLengthField(QualifiedName qualifiedName) {
        if (!qualifiedName.getName().getIdentifier().equals("length"))
            return false;
        @Nullable ITypeBinding binding = qualifiedName.getQualifier().resolveTypeBinding();
        return binding != null;
    }

    /**
     * Returns the position following the last character in the node; just a shortcut for adding the length to the start
     * position. Note that the end position may be (one) past the end of the source.
     *
     * @param node ASTNode in question
     * @return last position in the node + 1
     */
    public static int getEndPosition(ASTNode node) {
        return node.getStartPosition() + node.getLength();
    }

    /**
     * Returns the position following the last character in the last node in the list. Note that the end position may be
     * (one) past the end of the source.
     *
     * @param nodes list of nodes; each item in the list should actually be of a type that's a subclass of ASTNode
     * @return last position of the last node in the list + 1
     */
    public static int getEndPosition(List<?> nodes) {
        Object lastNodeObject = null;
        for (Object node : nodes)
            lastNodeObject = node;

        return getEndPosition((ASTNode) lastNodeObject);
    }

    public static String simpleNameFromQualifiedName(String qualifiedName) {
        int lastPeriodIndex = qualifiedName.lastIndexOf('.');
        if (lastPeriodIndex == -1)
            return qualifiedName;
        else return qualifiedName.substring(lastPeriodIndex) + 1;
    }

    public static boolean directlyImplementsInterface(ITypeBinding typeBinding, String desiredInterfaceQualifiedName) {
        for (ITypeBinding interfaceTypeBinding : typeBinding.getInterfaces()) {
            if (interfaceTypeBinding.getQualifiedName().equals(desiredInterfaceQualifiedName))
                return true;
        }

        return false;
    }

    public static boolean implementsInterface(ITypeBinding typeBinding, String interfaceQualifiedName) {
        while (typeBinding != null) {
            if (directlyImplementsInterface(typeBinding, interfaceQualifiedName))
                return true;
            typeBinding = typeBinding.getSuperclass();
        }

        return false;
    }

    public static boolean isFunctionalInterfaceImplementation(Context context, Type type) {
        ITypeBinding typeBinding = context.resolveTypeBinding(type);

        if (!typeBinding.isInterface())
            return false;

        int methodCount = typeBinding.getDeclaredMethods().length;
        if (methodCount != 1)
            return false;

        // TODO: Ensure no default implementation (I think) nor constants defined for the interface

        return true;
    }

    /**
     * Add all the wildcards referenced from a given type, searching recursively in the type definition to add all of
     * them.   That is, for a type like this:
     * <p>
     * "Foo< Bar<? extends Fizz>, <Blip <? super Pop>>, ? >"
     * <p>
     * 3 wildcard types would be added (<? extends Fizz>, <? super Pop>, and ?) since essentially the ? appears 3 times
     * the in the type definition above.
     *
     * @param type
     * @param wildcardTypes
     */
    public static void addWildcardTypes(Type type, ArrayList<WildcardType> wildcardTypes) {
        if (type.isParameterizedType()) {
            ParameterizedType parameterizedType = (ParameterizedType) type;

            for (Object typeArgumentObject : parameterizedType.typeArguments()) {
                Type typeArgument = (Type) typeArgumentObject;
                addWildcardTypes(typeArgument, wildcardTypes);
            }
        } else if (type.isWildcardType()) {
            WildcardType wildcardType = (WildcardType) type;

            wildcardTypes.add(wildcardType);
            @Nullable Type bound = wildcardType.getBound();

            if (bound != null)
                addWildcardTypes(bound, wildcardTypes);
        }
    }

    @FunctionalInterface
    public interface IProcessListElmt<T> {
        public void process(T elmt);
    }

    @FunctionalInterface
    public interface IProcessListElmtWithFirst<T> {
        public void process(T elmt, boolean first);
    }

    public static <T> void forEach(List list, IProcessListElmt<T> processList) {
        for (Object elmtObject : list) {
            T elmt = (T) elmtObject;
            processList.process(elmt);
        }
    }

    public static <T> void forEach(List list, IProcessListElmtWithFirst<T> listDo) {
        boolean first = true;
        for (Object elmtObject : list) {
            T elmt = (T) elmtObject;
            listDo.process(elmt, first);
            first = false;
        }
    }
}
