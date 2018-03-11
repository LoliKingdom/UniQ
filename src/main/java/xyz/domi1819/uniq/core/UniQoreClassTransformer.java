package xyz.domi1819.uniq.core;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import static org.objectweb.asm.Opcodes.*;

public class UniQoreClassTransformer implements IClassTransformer
{
    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes)
    {
        if (transformedName.equals("codechicken.nei.recipe.GuiCraftingRecipe"))
        {
            ClassReader classReader = new ClassReader(bytes);
            ClassNode classNode = new ClassNode();

            classReader.accept(classNode, 0);

            for (MethodNode method : classNode.methods)
            {
                if (method.name.equals("openRecipeGui"))
                {
                    InsnList body = method.instructions;

                    for (AbstractInsnNode instruction : body.toArray())
                    {
                        if (instruction instanceof FrameNode && ((FrameNode)instruction).type == F_SAME && instruction.getNext() != null && instruction.getNext().getOpcode() == GOTO)
                        {
                            InsnList list = new InsnList();

                            LabelNode cleanup = new LabelNode();
                            LabelNode end = new LabelNode();

                            list.add(new VarInsnNode(ALOAD, 1));
                            list.add(new InsnNode(ARRAYLENGTH));
                            list.add(new JumpInsnNode(IFLE, end));
                            list.add(new VarInsnNode(ALOAD, 1));
                            list.add(new InsnNode(ICONST_0));
                            list.add(new InsnNode(AALOAD));
                            list.add(new TypeInsnNode(CHECKCAST, "net/minecraft/item/ItemStack"));
                            list.add(new InsnNode(DUP));
                            list.add(new MethodInsnNode(INVOKESTATIC, "xyz/domi1819/uniq/NEIHelper", "getPreferredStack", "(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;", false));
                            list.add(new InsnNode(DUP2));
                            list.add(new MethodInsnNode(INVOKESTATIC, "xyz/domi1819/uniq/NEIHelper", "itemsEqual", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z", false));
                            list.add(new JumpInsnNode(IFNE, cleanup));
                            list.add(new VarInsnNode(ALOAD, 7));
                            list.add(new InsnNode(SWAP));
                            list.add(new VarInsnNode(ALOAD, 0));
                            list.add(new InsnNode(SWAP));
                            list.add(new InsnNode(ICONST_1));
                            list.add(new TypeInsnNode(ANEWARRAY, "java/lang/Object"));
                            list.add(new InsnNode(DUP_X1));
                            list.add(new InsnNode(SWAP));
                            list.add(new InsnNode(ICONST_0));
                            list.add(new InsnNode(SWAP));
                            list.add(new InsnNode(AASTORE));
                            list.add(new MethodInsnNode(INVOKEINTERFACE, "codechicken/nei/recipe/ICraftingHandler", "getRecipeHandler", "(Ljava/lang/String;[Ljava/lang/Object;)Lcodechicken/nei/recipe/ICraftingHandler;", true));
                            list.add(new InsnNode(DUP));
                            list.add(new MethodInsnNode(INVOKEINTERFACE, "codechicken/nei/recipe/ICraftingHandler", "numRecipes", "()I", true));
                            list.add(new JumpInsnNode(IFLE, cleanup));
                            list.add(new VarInsnNode(ALOAD, 5));
                            list.add(new InsnNode(SWAP));
                            list.add(new MethodInsnNode(INVOKEVIRTUAL, "java/util/ArrayList", "add", "(Ljava/lang/Object;)Z", false));
                            list.add(cleanup);
                            list.add(new InsnNode(POP2));
                            list.add(end);

                            body.insertBefore(instruction, list);

                            break;
                        }
                    }
                }
            }

            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);

            classNode.accept(classWriter);

            return classWriter.toByteArray();
        }

        return bytes;
    }
}
