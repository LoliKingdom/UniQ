package re.domi.uniq.core;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;

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
                        if (instruction instanceof FrameNode && ((FrameNode) instruction).type == F_SAME && instruction.getNext() != null && instruction.getNext().getOpcode() == GOTO)
                        {
                            InsnList list = new InsnList();

                            list.add(new VarInsnNode(ALOAD, 7));
                            list.add(new VarInsnNode(ALOAD, 0));
                            list.add(new VarInsnNode(ALOAD, 1));
                            list.add(new VarInsnNode(ALOAD, 5));
                            list.add(new MethodInsnNode(INVOKESTATIC, "codechicken/nei/recipe/GuiCraftingRecipe", "addUnifiedHandlers", "(Lcodechicken/nei/recipe/ICraftingHandler;Ljava/lang/String;[Ljava/lang/Object;Ljava/util/ArrayList;)V", false));

                            body.insert(instruction, list);

                            break;
                        }
                    }
                }
            }

            MethodNode method = new MethodNode();

            method.name = "addUnifiedHandlers";
            method.desc = "(Lcodechicken/nei/recipe/ICraftingHandler;Ljava/lang/String;[Ljava/lang/Object;Ljava/util/ArrayList;)V";
            method.access = ACC_PRIVATE | ACC_STATIC;
            method.exceptions = new ArrayList<>(0);

            InsnList body = new InsnList();
            LabelNode end = new LabelNode();

            body.add(new VarInsnNode(ALOAD, 2));
            body.add(new InsnNode(ARRAYLENGTH));
            body.add(new JumpInsnNode(IFLE, end));
            body.add(new VarInsnNode(ALOAD, 2));
            body.add(new InsnNode(ICONST_0));
            body.add(new InsnNode(AALOAD));
            body.add(new TypeInsnNode(INSTANCEOF, "net/minecraft/item/ItemStack"));
            body.add(new JumpInsnNode(IFEQ, end));
            body.add(new VarInsnNode(ALOAD, 2));
            body.add(new InsnNode(ICONST_0));
            body.add(new InsnNode(AALOAD));
            body.add(new TypeInsnNode(CHECKCAST, "net/minecraft/item/ItemStack"));
            body.add(new VarInsnNode(ASTORE, 4));
            body.add(new VarInsnNode(ALOAD, 4));
            body.add(new MethodInsnNode(INVOKESTATIC, "re/domi/uniq/NEIHelper", "getPreferredStack", "(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;", false));
            body.add(new VarInsnNode(ASTORE, 5));
            body.add(new VarInsnNode(ALOAD, 4));
            body.add(new VarInsnNode(ALOAD, 5));
            body.add(new MethodInsnNode(INVOKESTATIC, "re/domi/uniq/NEIHelper", "itemsEqual", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z", false));
            body.add(new JumpInsnNode(IFNE, end));
            body.add(new VarInsnNode(ALOAD, 0));
            body.add(new VarInsnNode(ALOAD, 1));
            body.add(new InsnNode(ICONST_1));
            body.add(new TypeInsnNode(ANEWARRAY, "java/lang/Object"));
            body.add(new InsnNode(DUP));
            body.add(new InsnNode(ICONST_0));
            body.add(new VarInsnNode(ALOAD, 5));
            body.add(new InsnNode(AASTORE));
            body.add(new MethodInsnNode(INVOKEINTERFACE, "codechicken/nei/recipe/ICraftingHandler", "getRecipeHandler", "(Ljava/lang/String;[Ljava/lang/Object;)Lcodechicken/nei/recipe/ICraftingHandler;", true));
            body.add(new VarInsnNode(ASTORE, 6));
            body.add(new VarInsnNode(ALOAD, 6));
            body.add(new MethodInsnNode(INVOKEINTERFACE, "codechicken/nei/recipe/ICraftingHandler", "numRecipes", "()I", true));
            body.add(new JumpInsnNode(IFLE, end));
            body.add(new VarInsnNode(ALOAD, 3));
            body.add(new VarInsnNode(ALOAD, 6));
            body.add(new MethodInsnNode(INVOKEVIRTUAL, "java/util/ArrayList", "add", "(Ljava/lang/Object;)Z", false));
            body.add(new InsnNode(POP));
            body.add(end);
            body.add(new InsnNode(RETURN));

            method.instructions = body;
            classNode.methods.add(method);

            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            classNode.accept(classWriter);

            return classWriter.toByteArray();
        }

        return bytes;
    }
}
