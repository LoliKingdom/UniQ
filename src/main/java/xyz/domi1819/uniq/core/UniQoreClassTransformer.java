package xyz.domi1819.uniq.core;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import xyz.domi1819.uniq.NEIHelper;

import static org.objectweb.asm.Opcodes.*;

@SuppressWarnings("unused")
public class UniQoreClassTransformer implements IClassTransformer
{
    @Override
    public byte[] transform(String name, String transformedName, byte[] classBytes)
    {
        if (transformedName.startsWith("codechicken.nei.recipe.TemplateRecipeHandler$CachedRecipe"))
        {
            ClassReader classReader = new ClassReader(classBytes);
            ClassNode classNode = new ClassNode();

            classReader.accept(classNode, 0);

            for (MethodNode method : classNode.methods)
            {
                if (method.name.equals("getCycledIngredients"))
                {
                    InsnList body = method.instructions;

                    for (AbstractInsnNode instruction : body.toArray())
                    {
                        if (instruction.getOpcode() == ARETURN)
                        {
                            body.insertBefore(instruction, new MethodInsnNode(INVOKESTATIC, Type.getInternalName(NEIHelper.class), "setPreferredStacks", "(Ljava/util/List;)V", false));
                            body.insertBefore(instruction, new VarInsnNode(ALOAD, 2));

                            break;
                        }
                    }
                }
            }

            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);

            classNode.accept(classWriter);

            return classWriter.toByteArray();
        }

        return classBytes;
    }
}
