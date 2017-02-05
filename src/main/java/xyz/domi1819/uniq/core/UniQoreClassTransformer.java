package xyz.domi1819.uniq.core;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import static org.objectweb.asm.Opcodes.*;

@SuppressWarnings("unused")
public class UniQoreClassTransformer implements IClassTransformer
{
    @Override
    public byte[] transform(String name, String transformedName, byte[] classBytes)
    {
        if (transformedName.equals("codechicken.nei.PositionedStack"))
        {
            ClassReader classReader = new ClassReader(classBytes);
            ClassNode classNode = new ClassNode();

            classReader.accept(classNode, 0);

            for (MethodNode method : classNode.methods)
            {
                if (method.name.equals("setPermutationToRender"))
                {
                    InsnList body = method.instructions;

                    for (AbstractInsnNode instruction : body.toArray())
                    {
                        if (instruction.getOpcode() == RETURN)
                        {
                            body.insertBefore(instruction, new VarInsnNode(ALOAD, 0));
                            body.insertBefore(instruction, new VarInsnNode(ALOAD, 0));
                            body.insertBefore(instruction, new FieldInsnNode(GETFIELD, "codechicken/nei/PositionedStack", "item", "Lnet/minecraft/item/ItemStack;"));
                            body.insertBefore(instruction, new MethodInsnNode(INVOKESTATIC, "xyz/domi1819/uniq/NEIHelper", "getPreferredStack", "(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;", false));
                            body.insertBefore(instruction, new FieldInsnNode(PUTFIELD, "codechicken/nei/PositionedStack", "item", "Lnet/minecraft/item/ItemStack;"));

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
