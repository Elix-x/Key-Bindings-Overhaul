package code.elix_x.coremods.keybindingsoverhaul.core;

import java.util.Iterator;

import net.minecraft.launchwrapper.IClassTransformer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.sun.javafx.scene.control.skin.LabeledImpl;

import cpw.mods.fml.common.gameevent.TickEvent;

public class KeysOverhaulTransformer implements IClassTransformer{

	/*
	 * KeyBinding
	 * Minecraft
	 * GameSettings
	 */
	
	public static Logger logger = LogManager.getLogger("KBO Core");

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if(name.equals(KeysOverhaulTranslator.getMapedClassName("client.settings.KeyBinding"))){
			logger.info("##################################################");
			logger.info("Patching KeyBinding");

			byte[] b = patchKeyBinding(name, bytes);

			logger.info("Patching KeyBinding Completed");
			logger.info("##################################################");
			return b;
		}
		if(name.equals(KeysOverhaulTranslator.getMapedClassName("client.Minecraft"))){
			logger.info("##################################################");
			logger.info("Patching Minecraft");

			byte[] b = patchMinecraft(name, bytes);

			logger.info("Patching Minecraft Completed");
			logger.info("##################################################");
			return b;
		}
		if(name.equals(KeysOverhaulTranslator.getMapedClassName("client.settings.GameSettings"))){
			logger.info("##################################################");
			logger.info("Patching GameSettings");

			byte[] b = patchGameSettings(name, bytes);

			logger.info("Patching GameSettings Completed");
			logger.info("##################################################");
			return b;
		}
		return bytes;
	}

	private byte[] patchGameSettings(String name, byte[] bytes) {
		String saveOptions = KeysOverhaulTranslator.getMapedMethodName("GameSettings", "func_74303_b", "saveOptions");
		String saveOptionsDesc = KeysOverhaulTranslator.getMapedMethodDesc("GameSettings", "func_74303_b", "()V");
		String loadOptions = KeysOverhaulTranslator.getMapedMethodName("GameSettings", "func_74300_a", "loadOptions");
		String loadOptionsDesc = KeysOverhaulTranslator.getMapedMethodDesc("GameSettings", "func_74300_a", "()V");

		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);

		for(MethodNode method : classNode.methods){
			if(method.name.equals(loadOptions) && method.desc.equals(loadOptionsDesc)){
				try{
					logger.info("**************************************************");
					logger.info("Patching loadOptions");

					InsnList list = new InsnList();
					list.add(new VarInsnNode(Opcodes.ALOAD, 0));
					list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "code.elix_x.coremods.keybindingsoverhaul.core.AsmHooks".replace(".", "/"), "loadSettings", "(L" + KeysOverhaulTranslator.getMapedClassName("client.settings.GameSettings").replace(".", "/") + ";)V", false));//					list.add(new JumpInsnNode(Opcodes.GOTO, gotol));
					list.add(new LabelNode());
					method.instructions.insert(list);

					logger.info("Patching loadOptions Completed");
					logger.info("**************************************************");
				}catch(Exception e){
					logger.error("Patching loadOptions Failed With Exception: ", e);
					logger.info("**************************************************");
				}
			}
			
			if(method.name.equals(saveOptions) && method.desc.equals(saveOptionsDesc)){
				try{
					logger.info("**************************************************");
					logger.info("Patching saveOptions");

					/*
					 * GETFIELD net/minecraft/client/settings/GameSettings.keyBindings : [Lnet/minecraft/client/settings/KeyBinding;
					 * ASTORE 2
					 */
					
					/*
					 * LDC "forceUnicodeFont:"
					 * INVOKEVIRTUAL java/io/PrintWriter.println (Ljava/lang/String;)V
					 */

					/*
					 *  INVOKEVIRTUAL java/io/PrintWriter.println (Ljava/lang/String;)V
					 */
					AbstractInsnNode methodCall = null;
					AbstractInsnNode skipTo = null;

					for(AbstractInsnNode currentNode : method.instructions.toArray()){
						/*if(currentNode.getOpcode() == Opcodes.GETFIELD){
							FieldInsnNode field = (FieldInsnNode) currentNode;
							if(field.owner.replace("/", ".").equals(KeysOverhaulTranslator.getMapedClassName("client.settings.GameSettings")) && field.name.equals(KeysOverhaulTranslator.getMapedMethodName("GameSettings", "field_74324_K", "keyBindings"))){
								methodCall = currentNode.getNext();
							}
						}*/
						if(currentNode.getOpcode() == Opcodes.INVOKEVIRTUAL){
							MethodInsnNode m = (MethodInsnNode) currentNode;
							if(m.owner.equals("java/io/PrintWriter") && m.name.equals("println") && m.desc.equals("(Ljava/lang/String;)V")){
								AbstractInsnNode node = currentNode;
								for(int i = 0; i < 11; i++){
									node = node.getPrevious();
									if(node.getOpcode() == Opcodes.LDC){
										LdcInsnNode ldc = (LdcInsnNode) node;
										if(ldc.cst instanceof String){
											if(ldc.cst.equals("key_")){
												skipTo = currentNode;
												break;
											}
											if(ldc.cst.equals("forceUnicodeFont:")){
												methodCall = currentNode;
											}
										}
									}
								}
							}
						}
					}

//					LabelNode gotol = new LabelNode();

					InsnList list = new InsnList();
					list.add(new LabelNode());
					list.add(new VarInsnNode(Opcodes.ALOAD, 1));
					list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "code.elix_x.coremods.keybindingsoverhaul.core.AsmHooks".replace(".", "/"), "saveSettings", "(Ljava/io/PrintWriter;)V", false));
//					list.add(new LabelNode());
//					list.add(new JumpInsnNode(Opcodes.GOTO, gotol));
					method.instructions.insert(methodCall, list);

//					method.instructions.insert(skipTo, gotol);

					logger.info("Patching saveOptions Completed");
					logger.info("**************************************************");
				}catch(Exception e){
					logger.error("Patching saveOptions Failed With Exception: ", e);
					logger.info("**************************************************");
				}
			}
		}

		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
//		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		classNode.accept(writer);
		return writer.toByteArray();
	}

	private byte[] patchMinecraft(String name, byte[] bytes) {
		String runTick = KeysOverhaulTranslator.getMapedMethodName("Minecraft", "func_71407_l", "runTick");
		String runTickDesc = KeysOverhaulTranslator.getMapedMethodDesc("Minecraft", "func_71407_l", "()V");

		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);

		for(MethodNode method : classNode.methods){

			if(method.name.equals(runTick) && method.desc.equals(runTickDesc)){
				try{
					logger.info("**************************************************");
					logger.info("Patching runTick");

					{
						/*
						 *  INVOKESTATIC org/lwjgl/input/Mouse.getEventButton ()I
						 *  ISTORE 4
						 */

						/*
						 *  INVOKESTATIC net/minecraft/client/settings/KeyBinding.onTick (I)V
						 */

						AbstractInsnNode methodCall = null;
						AbstractInsnNode skipTo = null;

						for(AbstractInsnNode currentNode : method.instructions.toArray()){
							if(currentNode.getOpcode() == Opcodes.INVOKESTATIC){
								MethodInsnNode m = (MethodInsnNode) currentNode;
								if(m.owner.equals("org/lwjgl/input/Mouse") && m.name.equals("getEventButton") && m.desc.equals("()I")){
									methodCall = currentNode.getNext();
								}
								if(m.owner.replace("/", ".").equals(KeysOverhaulTranslator.getMapedClassName("client.settings.KeyBinding")) && m.name.equals(KeysOverhaulTranslator.getMapedMethodName("KeyBinding", "func_74507_a", "onTick"))){
									skipTo = currentNode;
									break;
								}
							}
						}

						LabelNode gotol = new LabelNode();

						InsnList list = new InsnList();
						list.add(new LabelNode());
						//					list.add(new VarInsnNode(Opcodes.ILOAD, 4));
						//					list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "code.elix_x.coremods.keybindingsoverhaul.keys.AsmHooks", "onMouseEventCheck", "", false));
						list.add(new JumpInsnNode(Opcodes.GOTO, gotol));
						method.instructions.insert(methodCall, list);

						method.instructions.insert(skipTo, gotol);
					}

					{
						/*
						 *  INVOKESTATIC org/lwjgl/input/Keyboard.next ()Z
						 *  IFEQ L83
						 */

						/*
						 * INVOKESTATIC org/lwjgl/input/Keyboard.getEventKey ()I
						 * INVOKESTATIC net/minecraft/client/settings/KeyBinding.onTick (I)V
						 */
						AbstractInsnNode methodCall = null;
						AbstractInsnNode skipTo = null;

						for(AbstractInsnNode currentNode : method.instructions.toArray()){
							if(currentNode.getOpcode() == Opcodes.INVOKESTATIC){
								MethodInsnNode m = (MethodInsnNode) currentNode;
								if(m.owner.equals("org/lwjgl/input/Keyboard") && m.name.equals("next") && m.desc.equals("()Z")){
									methodCall = currentNode.getNext();
								}
								if(m.owner.replace("/", ".").equals(KeysOverhaulTranslator.getMapedClassName("client.settings.KeyBinding")) && m.name.equals(KeysOverhaulTranslator.getMapedMethodName("KeyBinding", "func_74507_a", "onTick"))){
									if(m.getPrevious().getOpcode() == Opcodes.INVOKESTATIC){
										MethodInsnNode mm = (MethodInsnNode) m.getPrevious();
										if(mm.owner.equals("org/lwjgl/input/Keyboard") && mm.name.equals("getEventKey") && mm.desc.equals("()I")){
											skipTo = currentNode;
											break;
										}
									}
								}
							}
						}

						LabelNode gotol = new LabelNode();

						InsnList list = new InsnList();
						list.add(new LabelNode());
						//					list.add(new VarInsnNode(Opcodes.ILOAD, 4));
						//					list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "code.elix_x.coremods.keybindingsoverhaul.keys.AsmHooks", "onMouseEventCheck", "", false));
						list.add(new JumpInsnNode(Opcodes.GOTO, gotol));
						method.instructions.insert(methodCall, list);

						method.instructions.insert(skipTo, gotol);
					}

					logger.info("Patching runTick Completed");
					logger.info("**************************************************");
				}catch(Exception e){
					logger.error("Patching runTick Failed With Exception: ", e);
					logger.info("**************************************************");
				}
			}
		}

		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);
		return writer.toByteArray();
	}


	/*private void patchKeyboardSection(MethodNode method) {

	}

	private void patchMouseSection(MethodNode method) {
		AbstractInsnNode targetNode = null;

		for(AbstractInsnNode currentNode : method.instructions.toArray()){
			if(currentNode.getOpcode() == Opcodes.INVOKEVIRTUAL){
				MethodInsnNode m = (MethodInsnNode) currentNode;
				if(m.owner.replace("/", ".").equals(KeysOverhaulTranslator.getMapedClassName("profiler.Profiler")) && m.name.equals(KeysOverhaulTranslator.getMapedMethodName("Profiler", "func_76318_c", "endStartSection"))){
					if(m.getPrevious().getOpcode() == Opcodes.LDC){
						LdcInsnNode ldc = (LdcInsnNode) m.getPrevious();
						if(ldc.cst instanceof String){
							if(ldc.cst.equals("mouse")){
								targetNode = currentNode;
								break;
							}
						}
					}
				}
			}
		}

		LabelNode end = new LabelNode();

		InsnList list = new InsnList();
		list.add(new LabelNode());
		list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "code/elix_x/coremods/keysoverhaul/keys/AsmHooks", "mouseSection", "()V"));
		list.add(new JumpInsnNode(Opcodes.GOTO, end));

		method.instructions.insert(targetNode, list);

		AbstractInsnNode endTargetNode = null;

		for(AbstractInsnNode currentNode : method.instructions.toArray()){

		}
	}*/

	private byte[] patchKeyBinding(String name, byte[] bytes) {
		String onTick = KeysOverhaulTranslator.getMapedMethodName("KeyBinding", "func_74507_a", "onTick");
		String onTickDesc = KeysOverhaulTranslator.getMapedMethodDesc("KeyBinding", "func_74507_a", "(I)V");
		String setKeyBindState = KeysOverhaulTranslator.getMapedMethodName("KeyBinding", "func_74510_a", "setKeyBindState");
		String setKeyBindStateDesc = KeysOverhaulTranslator.getMapedMethodDesc("KeyBinding", "func_74510_a", "(IZ)V");
		String resetKeyBindingArrayAndHash = KeysOverhaulTranslator.getMapedMethodName("KeyBinding", "func_74508_b", "resetKeyBindingArrayAndHash");
		String resetKeyBindingArrayAndHashDesc = KeysOverhaulTranslator.getMapedMethodDesc("KeyBinding", "func_74508_b", "()V");
		String init = "<init>";

		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);

		for(MethodNode method : classNode.methods){

			/*if(method.name.equals(onTick) && method.desc.equals(onTickDesc)){
				try{
					logger.info("**************************************************");
					logger.info("Patching onTick");

					InsnList list = new InsnList();
					list.add(new LabelNode());
					list.add(new VarInsnNode(Opcodes.ILOAD, 0));
					list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "code/elix_x/coremods/keysoverhaul/keys/AsmHooks", "onTick", "(I)V"));
					list.add(new InsnNode(Opcodes.RETURN));
					method.instructions.insertBefore(method.instructions.getFirst(), list);

					logger.info("Patching onTick Completed");
					logger.info("**************************************************");
				}catch(Exception e){
					logger.info("Patching onTick Failed With Exception:");
					e.printStackTrace();
					logger.info("**************************************************");
				}
			}

			if(method.name.equals(setKeyBindState) && method.desc.equals(setKeyBindStateDesc)){
				try{
					logger.info("**************************************************");
					logger.info("Patching setKeyBindState");

					InsnList list = new InsnList();
					list.add(new LabelNode());
					list.add(new VarInsnNode(Opcodes.ILOAD, 0));
					list.add(new VarInsnNode(Opcodes.ILOAD, 1));
					list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "code/elix_x/coremods/keysoverhaul/keys/AsmHooks", "setKeyBindState", "(IZ)V"));
					list.add(new InsnNode(Opcodes.RETURN));
					method.instructions.insertBefore(method.instructions.getFirst(), list);

					logger.info("Patching setKeyBindState Completed");
					logger.info("**************************************************");
				}catch(Exception e){
					logger.info("Patching setKeyBindState Failed With Exception:");
					e.printStackTrace();
					logger.info("**************************************************");
				}
			}

			if(method.name.equals(resetKeyBindingArrayAndHash) && method.desc.equals(resetKeyBindingArrayAndHashDesc)){
				try{
					logger.info("**************************************************");
					logger.info("Patching resetKeyBindingArrayAndHash");

					InsnList list = new InsnList();
					list.add(new LabelNode());
					list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "code/elix_x/coremods/keysoverhaul/keys/AsmHooks", "resetKeyBindingArrayAndHash", "()V"));
					//					list.add(new InsnNode(Opcodes.RETURN));
					method.instructions.insertBefore(method.instructions.getFirst(), list);

					logger.info("Patching resetKeyBindingArrayAndHash Completed");
					logger.info("**************************************************");
				}catch(Exception e){
					logger.info("Patching resetKeyBindingArrayAndHash Failed With Exception:");
					e.printStackTrace();
					logger.info("**************************************************");
				}
			}*/

			if(method.name.equals(init)){
				try{
					logger.info("**************************************************");
					logger.info("Patching <init>");

					AbstractInsnNode targetNode = null;

					for(AbstractInsnNode currentNode : method.instructions.toArray()){
						if(currentNode.getOpcode() == Opcodes.INVOKEVIRTUAL){
							MethodInsnNode m = (MethodInsnNode) currentNode;
							if(m.owner.replace("/", ".").equals(KeysOverhaulTranslator.getMapedClassName("util.IntHashMap"))){
								targetNode = currentNode;
								break;
							}
						}
					}

					InsnList list = new InsnList();
					list.add(new LabelNode());
					list.add(new VarInsnNode(Opcodes.ILOAD, 2));
					list.add(new VarInsnNode(Opcodes.ALOAD, 0));
					logger.info(KeysOverhaulTranslator.getMapedClassName("client.settings.KeyBinding"));
					list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "code/elix_x/coremods/keybindingsoverhaul/core/AsmHooks", "register", "(IL" + KeysOverhaulTranslator.getMapedClassName("client.settings.KeyBinding").replace(".", "/") + ";)V"));
					method.instructions.insert(targetNode, list);

					logger.info("Patching <init> Completed");
					logger.info("**************************************************");
				}catch(Exception e){
					logger.error("Patching <init> Failed With Exception: ", e);
					logger.info("**************************************************");
				}
			}
		}

		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);
		return writer.toByteArray();
	}

	//runTickOld 
	//
	//	/*patchMouseSection(method);
	//	patchKeyboardSection(method);*/
	//	
	//	AbstractInsnNode targetNode = null;
	//
	//	for(AbstractInsnNode currentNode : method.instructions.toArray()){
	//		if(currentNode.getOpcode() == Opcodes.INVOKEVIRTUAL){
	//			MethodInsnNode m = (MethodInsnNode) currentNode;
	//			if(m.owner.replace("/", ".").equals(KeysOverhaulTranslator.getMapedClassName("profiler.Profiler")) && m.name.equals(KeysOverhaulTranslator.getMapedMethodName("Profiler", "func_76318_c", "endStartSection"))){
	//				if(m.getPrevious().getOpcode() == Opcodes.LDC){
	//					LdcInsnNode ldc = (LdcInsnNode) m.getPrevious();
	//					if(ldc.cst instanceof String){
	//						if(ldc.cst.equals("mouse")){
	//							targetNode = currentNode;
	//							break;
	//						}
	//					}
	//				}
	//			}
	//		}
	//	}
	//	
	//	LabelNode end = new LabelNode();
	//	
	//	InsnList list = new InsnList();
	//	list.add(new LabelNode());
	//	list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "code/elix_x/coremods/keysoverhaul/keys/AsmHooks", "runTick", "()V"));
	//	list.add(new JumpInsnNode(Opcodes.GOTO, end));
	//	
	//	method.instructions.insert(targetNode, list);
	//	
	//	AbstractInsnNode endTargetNode = null;
	//	
	//	/*
	//	 *  FRAME FULL [net/minecraft/client/Minecraft java/util/Queue T T I I] [net/minecraft/client/Minecraft I]
	//	 *  INVOKESPECIAL net/minecraft/client/Minecraft.func_147115_a (Z)V
	//	 */
	//	for(AbstractInsnNode currentNode : method.instructions.toArray()){
	//		if(currentNode.getOpcode() == Opcodes.INVOKESPECIAL){
	//			MethodInsnNode m = (MethodInsnNode) currentNode;
	//			if(m.owner.replace("/", ".").equals(KeysOverhaulTranslator.getMapedClassName("client.Minecraft")) && m.name.equals(KeysOverhaulTranslator.getMapedMethodName("Minecraft", "func_147115_a", "func_147115_a"))){
	//				/*if(m.getPrevious().getOpcode() == Opcodes.LDC){
	//					LdcInsnNode ldc = (LdcInsnNode) m.getPrevious();
	//					if(ldc.cst instanceof String){
	//						if(ldc.cst.equals("mouse")){
	//							targetNode = currentNode;
	//							break;
	//						}
	//					}
	//				}*/
	//				endTargetNode = currentNode;
	//				break;
	//			}
	//		}
	//	}
	//	
	//	method.instructions.insert(endTargetNode, end);
}
