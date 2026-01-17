package net.jomcraft.noitemdespawn;


import net.minecraft.item.Item;
import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.Level;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

import static org.objectweb.asm.Opcodes.*;

import java.io.*;
import java.util.Arrays;

public class NoItemDespawnClassTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        return new byte[0];
    }
   /* private static final String[] classesToTransform = {
            "net.minecraft.entity.item.EntityItem",
            "net.minecraft.item.Item",
    };

    public static boolean active = false;

    public static boolean gameObf;

    @Override
    public byte[] transform(String name, String transformedName, byte[] classToTransform) {
        boolean isObfuscated = !name.equals(transformedName);

        int c_index = Arrays.asList(classesToTransform).indexOf(transformedName);
        if (!gameObf && isObfuscated)
            gameObf = true;

        return c_index != -1 ? transform(c_index, classToTransform, isObfuscated, name, transformedName) : classToTransform;
    }

    public void createConfig(File fl) {
        try {
            fl.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(fl));
            if (new File("./libraries").exists()) {
                if (new File("./libraries/net/minecraftforge/forge").exists()) {

                    bw.write("#NoItemDespawn - External config\nAllow-mod = false");
                } else {
                    bw.write("#NoItemDespawn - External config\nAllow-mod = true");
                }

            } else {
                bw.write("#NoItemDespawn - External config\nAllow-mod = false");
            }

            bw.flush();
            bw.close();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }*/

  /*  private byte[] transform(int c_index, byte[] classToTransform, boolean isObfuscated, String name, String transformedName) {

        NoItemDespawn.log(Level.INFO, "Transforming: " + classesToTransform[c_index]);
        boolean infinit = false;
        try {
            File fl = new File("./NID-Sideconfig.txt");
            File configFl = new File("./config/noitemdespawn.cfg");
            if (!fl.exists()) {
                createConfig(fl);
            } else {
                BufferedReader br = new BufferedReader(new FileReader(fl));
                String line = null;
                while ((line = br.readLine()) != null) {

                    if (line.startsWith("Allow-mod")) {
                        line = line.substring(9);
                        line = line.replace("=", "");
                        line = line.replace(" ", "");
                        if (line.equals("true")) {

                            active = true;

                        }
                        break;
                    }
                }

                br.close();

                ConfigFile config = new ConfigFile(configFl);
                Integer dTime = config.cooldown;
                if (dTime < 0) {
                    dTime = Integer.MAX_VALUE;
                    infinit = true;
                }

                NoItemDespawn.instance().despawnTime = dTime;

            }
        } catch (IOException e) {

            e.printStackTrace();
        }

        try {

            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader(classToTransform);
            classReader.accept(classNode, 0);
            if (active) {
                switch (c_index) {

                    case 0:
                       // addFieldBurn(classNode, isObfuscated);
                        transformDSEvent(classNode, isObfuscated, infinit);
                        transformFieldGlobal(classNode, isObfuscated);
                        transformMethodGlobal(classNode, isObfuscated);

                        //addMethodBurn(classNode, isObfuscated);
                        //addMethodOverlay(classNode, isObfuscated);
                    case 1:
                       // transformItemClass(classNode, isObfuscated);
                        break;
                }
            } else {
                NoItemDespawn.log(Level.INFO, "Feature has been disabled!");
            }
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classToTransform;
    }*/

    /*private static void addMethodOverlay(ClassNode mainClass, boolean isObfuscated) {
        if (mainClass.name.equals("net/minecraft/entity/item/EntityItem") || mainClass.name.equals("xk")) {
            MethodNode mn = new MethodNode(ACC_PUBLIC, (isObfuscated ? "aA" : "func_90999_ad"), "()Z", null, null);
            AnnotationVisitor av0 = mn.visitAnnotation("Lcpw/mods/fml/relauncher/SideOnly;", true);
            av0.visitEnum("value", "Lcpw/mods/fml/relauncher/Side;", "CLIENT");

            Label l0 = new Label();
            mn.visitLabel(l0);
            mn.visitVarInsn(ALOAD, 0);
            mn.visitFieldInsn(GETFIELD, (isObfuscated ? "xk" : "net/minecraft/entity/item/EntityItem"), "glowing", "Z");
            Label l1 = new Label();
            mn.visitJumpInsn(IFEQ, l1);
            Label l2 = new Label();
            mn.visitLabel(l2);
            mn.visitInsn(ICONST_1);
            mn.visitInsn(IRETURN);
            mn.visitLabel(l1);
            mn.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            mn.visitVarInsn(ALOAD, 0);
            mn.visitMethodInsn(INVOKEVIRTUAL, (isObfuscated ? "xk" : "net/minecraft/entity/item/EntityItem"), (isObfuscated ? "al" : "func_70027_ad"), "()Z", false);
            mn.visitInsn(IRETURN);

            mainClass.methods.add(mn);

            NoItemDespawn.log(Level.INFO, "Added fire method!");
        }
    }*/

    /*private static void addMethodBurn(ClassNode mainClass, boolean isObfuscated) {
        if (mainClass.name.equals("net/minecraft/entity/item/EntityItem") || mainClass.name.equals("xk")) {
            MethodNode mn = new MethodNode(ACC_PUBLIC, (isObfuscated ? "c" : "func_70070_b"), "(F)I", null, null);
            AnnotationVisitor av0 = mn.visitAnnotation("Lcpw/mods/fml/relauncher/SideOnly;", true);
            av0.visitEnum("value", "Lcpw/mods/fml/relauncher/Side;", "CLIENT");

            Label l0 = new Label();
            mn.visitLabel(l0);
            mn.visitVarInsn(ALOAD, 0);
            mn.visitFieldInsn(GETFIELD, (isObfuscated ? "xk" : "net/minecraft/entity/item/EntityItem"), "glowing", "Z");
            Label l1 = new Label();
            mn.visitJumpInsn(IFEQ, l1);
            Label l2 = new Label();
            mn.visitLabel(l2);
            mn.visitLdcInsn(new Integer(15728880));
            mn.visitInsn(IRETURN);
            mn.visitLabel(l1);
            mn.visitLineNumber(153, l1);
            mn.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            mn.visitVarInsn(ALOAD, 0);
            mn.visitFieldInsn(GETFIELD, (isObfuscated ? "xk" : "net/minecraft/entity/item/EntityItem"), (isObfuscated ? "s" : "field_70165_t"), "D");
            mn.visitMethodInsn(INVOKESTATIC, (isObfuscated ? "qh" : "net/minecraft/util/MathHelper"), (isObfuscated ? "c" : "func_76128_c"), "(D)I", false);
            mn.visitVarInsn(ISTORE, 2);
            Label l3 = new Label();
            mn.visitLabel(l3);
            mn.visitVarInsn(ALOAD, 0);
            mn.visitFieldInsn(GETFIELD, (isObfuscated ? "xk" : "net/minecraft/entity/item/EntityItem"), (isObfuscated ? "u" : "field_70161_v"), "D");
            mn.visitMethodInsn(INVOKESTATIC, (isObfuscated ? "qh" : "net/minecraft/util/MathHelper"), (isObfuscated ? "c" : "func_76128_c"), "(D)I", false);
            mn.visitVarInsn(ISTORE, 3);
            Label l4 = new Label();
            mn.visitLabel(l4);
            mn.visitVarInsn(ALOAD, 0);
            mn.visitFieldInsn(GETFIELD, (isObfuscated ? "xk" : "net/minecraft/entity/item/EntityItem"), (isObfuscated ? "o" : "field_70170_p"), (isObfuscated ? "Lahb;" : "Lnet/minecraft/world/World;"));
            mn.visitVarInsn(ILOAD, 2);
            mn.visitInsn(ICONST_0);
            mn.visitVarInsn(ILOAD, 3);
            mn.visitMethodInsn(INVOKEVIRTUAL, (isObfuscated ? "ahb" : "net/minecraft/world/World"), (isObfuscated ? "d" : "func_72899_e"), "(III)Z", false);
            Label l5 = new Label();
            mn.visitJumpInsn(IFEQ, l5);


            Label l6 = new Label();
            mn.visitLabel(l6);
            mn.visitVarInsn(ALOAD, 0);
            mn.visitFieldInsn(GETFIELD, (isObfuscated ? "xk" : "net/minecraft/entity/item/EntityItem"), (isObfuscated ? "C" : "field_70121_D"), (isObfuscated ? "Lazt;" : "Lnet/minecraft/util/AxisAlignedBB;"));
            mn.visitFieldInsn(GETFIELD, (isObfuscated ? "azt" : "net/minecraft/util/AxisAlignedBB"), (isObfuscated ? "e" : "field_72337_e"), "D");
            mn.visitVarInsn(ALOAD, 0);
            mn.visitFieldInsn(GETFIELD, (isObfuscated ? "xk" : "net/minecraft/entity/item/EntityItem"), (isObfuscated ? "C" : "field_70121_D"), (isObfuscated ? "Lazt;" : "Lnet/minecraft/util/AxisAlignedBB;"));
            mn.visitFieldInsn(GETFIELD, (isObfuscated ? "azt" : "net/minecraft/util/AxisAlignedBB"), (isObfuscated ? "b" : "field_72338_b"), "D");
            mn.visitInsn(DSUB);
            mn.visitLdcInsn(new Double("0.66"));
            mn.visitInsn(DMUL);
            mn.visitVarInsn(DSTORE, 4);
            Label l7 = new Label();
            mn.visitLabel(l7);
            mn.visitVarInsn(ALOAD, 0);
            mn.visitFieldInsn(GETFIELD, (isObfuscated ? "xk" : "net/minecraft/entity/item/EntityItem"), (isObfuscated ? "t" : "field_70163_u"), "D");
            mn.visitVarInsn(ALOAD, 0);
            mn.visitFieldInsn(GETFIELD, (isObfuscated ? "xk" : "net/minecraft/entity/item/EntityItem"), (isObfuscated ? "L" : "field_70129_M"), "F");
            mn.visitInsn(F2D);
            mn.visitInsn(DSUB);
            mn.visitVarInsn(DLOAD, 4);
            mn.visitInsn(DADD);
            mn.visitMethodInsn(INVOKESTATIC, (isObfuscated ? "qh" : "net/minecraft/util/MathHelper"), (isObfuscated ? "c" : "func_76128_c"), "(D)I", false);
            mn.visitVarInsn(ISTORE, 6);
            Label l8 = new Label();
            mn.visitLabel(l8);
            mn.visitVarInsn(ALOAD, 0);
            mn.visitFieldInsn(GETFIELD, (isObfuscated ? "xk" : "net/minecraft/entity/item/EntityItem"), (isObfuscated ? "o" : "field_70170_p"), (isObfuscated ? "Lahb;" : "Lnet/minecraft/world/World;"));
            mn.visitVarInsn(ILOAD, 2);
            mn.visitVarInsn(ILOAD, 6);
            mn.visitVarInsn(ILOAD, 3);
            mn.visitInsn(ICONST_0);
            mn.visitMethodInsn(INVOKEVIRTUAL, (isObfuscated ? "ahb" : "net/minecraft/world/World"), (isObfuscated ? "c" : "func_72802_i"), "(IIII)I", false);
            mn.visitInsn(IRETURN);
            mn.visitLabel(l5);
            mn.visitFrame(Opcodes.F_APPEND, 2, new Object[]{Opcodes.INTEGER, Opcodes.INTEGER}, 0, null);
            mn.visitInsn(ICONST_0);
            mn.visitInsn(IRETURN);

            mainClass.methods.add(mn);

            NoItemDespawn.log(Level.INFO, "Added glowing method!");
        }
    }*/

    /*private static void addFieldBurn(ClassNode mainClass, boolean isObfuscated) {
        if (isObfuscated) {
            mainClass.visitField(ACC_PUBLIC, "glowing", "Z", null, ICONST_0);
            NoItemDespawn.log(Level.INFO, "Added glowing field!");
        }
    }*/
/*
    private static void transformFieldGlobal(ClassNode mainClass, boolean isObfuscated) {
        final String FIELD_NAME = isObfuscated ? "<init>" : "<init>";

        final String FIELD_NAME_OBF = isObfuscated ? "(Lahb;DDD)V" : "(Lnet/minecraft/world/World;DDD)V";

        for (MethodNode method : mainClass.methods) {
            if (method.name.equals(FIELD_NAME) && method.desc.equals(FIELD_NAME_OBF)) {

                AbstractInsnNode targetNode = null;
                for (AbstractInsnNode instruction : method.instructions.toArray()) {
                    if (instruction.getOpcode() == ALOAD && instruction.getNext().getOpcode() == SIPUSH) {

                        targetNode = instruction.getNext();
                        break;
                    }
                }

                if (targetNode != null) {

                    method.instructions.insert(targetNode.getNext(), new FieldInsnNode(GETSTATIC, "net/jomcraft/noitemdespawn/NoItemDespawn", "despawnTime", "I"));
                    method.instructions.insert(targetNode.getNext(), new InsnNode(POP));
                    method.instructions.insert(targetNode.getNext(), new MethodInsnNode(INVOKESTATIC, "net/jomcraft/noitemdespawn/NoItemDespawn", "instance", "()Lnet/jomcraft/noitemdespawn/NoItemDespawn;", false));

                    method.instructions.remove(targetNode.getNext());
                    NoItemDespawn.log(Level.INFO, "Transformed global fields!");

                }

            }
        }
    }

    private static void transformMethodGlobal(ClassNode mainClass, boolean isObfuscated) {

        final String METHOD_NAME = isObfuscated ? "<init>" : "<init>";

        final String METHOD_NAME_OBF = isObfuscated ? "(Lahb;DDD)V" : "(Lnet/minecraft/world/World;DDD)V";

        for (MethodNode method : mainClass.methods) {
            if (method.name.equals(METHOD_NAME) && method.desc.equals(METHOD_NAME_OBF)) {
                AbstractInsnNode targetNode = null;
                for (AbstractInsnNode instruction : method.instructions.toArray()) {

                    if (instruction.getOpcode() == SIPUSH && ((IntInsnNode) instruction).operand == 6000) {
                        targetNode = instruction;
                        break;
                    }
                }

                if (targetNode != null) {

                    method.instructions.insert(targetNode, new FieldInsnNode(GETSTATIC, "net/jomcraft/noitemdespawn/NoItemDespawn", "despawnTime", "I"));
                    method.instructions.insert(targetNode, new InsnNode(POP));
                    method.instructions.insert(targetNode, new MethodInsnNode(INVOKESTATIC, "net/jomcraft/noitemdespawn/NoItemDespawn", "instance", "()Lnet/jomcraft/noitemdespawn/NoItemDespawn;", false));

                    method.instructions.remove(targetNode);
                    NoItemDespawn.log(Level.INFO, "Transformed global methods!");
                }

            }
        }

    }*/

    /*private static void transformItemClass(ClassNode mainClass, boolean isObfuscated) {

        final String CLASS_NAME = isObfuscated ? "getEntityLifespan" : "getEntityLifespan";

        final String CLASS_NAME_OBF = isObfuscated ? "(Ladd;Lahb;)I" : "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;)I";

        for (MethodNode method : mainClass.methods) {
            if (method.name.equals(CLASS_NAME) && method.desc.equals(CLASS_NAME_OBF)) {

                if (NIDCompatibility.isCauldronServer()) {

                    method.instructions.clear();
                    method.visitFieldInsn(GETSTATIC, "net/jomcraft/noitemdespawn/NoItemDespawn", "despawnTime", "I");
                    method.visitInsn(IRETURN);

                    NoItemDespawn.log(Level.INFO, "Transformed item class!");

                } else {

                    AbstractInsnNode targetNode = null;
                    for (AbstractInsnNode instruction : method.instructions.toArray()) {
                        if (instruction.getOpcode() == SIPUSH) {

                            if (((IntInsnNode) instruction).operand == 6000) {
                                targetNode = instruction;
                                break;
                            }

                        }
                    }

                    if (targetNode != null) {

                        method.instructions.insert(targetNode, new FieldInsnNode(GETSTATIC, "net/jomcraft/noitemdespawn/NoItemDespawn", "despawnTime", "I"));
                        method.instructions.insert(targetNode, new InsnNode(POP));
                        method.instructions.insert(targetNode, new MethodInsnNode(INVOKESTATIC, "net/jomcraft/noitemdespawn/NoItemDespawn", "instance", "()Lnet/jomcraft/noitemdespawn/NoItemDespawn;", false));
                        method.instructions.remove(targetNode);

                        NoItemDespawn.log(Level.INFO, "Transformed item class!");
                    }
                }

            }
        }

    }*/
/*
    private static void transformDSEvent(ClassNode mainClass, boolean isObfuscated, boolean infinit) {

        final String EVENT_NAME = isObfuscated ? "h" : "func_70071_h_";

        final String EVENT_NAME_OBF = isObfuscated ? "()V" : "()V";

        for (MethodNode method : mainClass.methods) {
            if (method.name.equals(EVENT_NAME) && method.desc.equals(EVENT_NAME_OBF)) {

                if (NIDCompatibility.isCauldronServer()) {

                    if (!infinit) {

                        AbstractInsnNode targetNode = null;
                        for (AbstractInsnNode instruction : method.instructions.toArray()) {
                            if (instruction.getOpcode() == IFNONNULL) {

                                targetNode = instruction.getNext().getNext();
                                break;

                            }
                        }

                        if (targetNode != null) {

                            AbstractInsnNode tn2 = targetNode;
                            for (int i = 0; i < 4; i++) {

                                method.instructions.remove(tn2.getPrevious());
                                tn2 = tn2.getNext();
                            }

                            method.instructions.insertBefore(tn2.getPrevious(), new MethodInsnNode(INVOKESTATIC, "net/jomcraft/noitemdespawn/NoItemDespawn", "instance", "()Lnet/jomcraft/noitemdespawn/NoItemDespawn;", false));
                            method.instructions.insertBefore(tn2.getPrevious(), new InsnNode(POP));
                            method.instructions.insertBefore(tn2.getPrevious(), new FieldInsnNode(GETSTATIC, "net/jomcraft/noitemdespawn/NoItemDespawn", "despawnTime", "I"));

                            NoItemDespawn.log(Level.INFO, "Transformed event method! - > Cauldron!");
                        }


                    } else {

                        AbstractInsnNode targetNode = null;
                        for (AbstractInsnNode instruction : method.instructions.toArray()) {

                            if (instruction.getOpcode() == ALOAD && instruction.getNext().getOpcode() == INVOKEVIRTUAL && instruction.getNext().getNext().getOpcode() == BIPUSH && instruction.getNext().getNext().getNext().getNext().getOpcode() == ASTORE) {

                                if (((VarInsnNode) instruction.getNext().getNext().getNext().getNext()).var == 6) {

                                    targetNode = instruction.getPrevious();
                                    break;
                                }

                            }
                        }

                        if (targetNode != null) {

                            for (int i = 0; i < 106; i++) {

                                method.instructions.remove(targetNode.getPrevious());
                                targetNode = targetNode.getNext();
                            }

                            NoItemDespawn.log(Level.INFO, "Transformed event method! - > Cauldron");

                        }

                    }

                } else {
                    if (infinit) {
                        AbstractInsnNode targetNode = null;
                        for (AbstractInsnNode instruction : method.instructions.toArray()) {

                            if (instruction.getOpcode() == ALOAD && instruction.getNext().getOpcode() == INVOKEVIRTUAL && instruction.getNext().getNext().getOpcode() == BIPUSH && instruction.getNext().getNext().getNext().getNext().getOpcode() == ASTORE) {

                                if (((VarInsnNode) instruction.getNext().getNext().getNext().getNext()).var == 4) {

                                    targetNode = instruction;
                                    break;
                                }

                            }
                        }

                        if (targetNode != null) {

                            for (int i = 0; i < 85; i++) {

                                method.instructions.remove(targetNode.getPrevious());
                                targetNode = targetNode.getNext();
                            }

                            NoItemDespawn.log(Level.INFO, "Transformed event method!");
                        }

                    } else {


                        AbstractInsnNode targetNode = null;
                        for (AbstractInsnNode instruction : method.instructions.toArray()) {
                            if (instruction.getOpcode() == SIPUSH) {
                                if (((IntInsnNode) instruction).operand == 6000) {
                                    targetNode = instruction;
                                    break;
                                }

                            }
                        }

                        if (targetNode != null) {

                            method.instructions.insert(targetNode, new FieldInsnNode(GETSTATIC, "net/jomcraft/noitemdespawn/NoItemDespawn", "despawnTime", "I"));
                            method.instructions.insert(targetNode, new InsnNode(POP));
                            method.instructions.insert(targetNode, new MethodInsnNode(INVOKESTATIC, "net/jomcraft/noitemdespawn/NoItemDespawn", "instance", "()Lnet/jomcraft/noitemdespawn/NoItemDespawn;", false));

                            method.instructions.remove(targetNode);
                            NoItemDespawn.log(Level.INFO, "Transformed event method!");
                        }

                    }
                }

            }
        }

    }
*/
}
