package com.gestankbratwurst.core.mmcore.resourcepack.skins;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.gestankbratwurst.core.mmcore.resourcepack.packing.BoxedFontChar;
import com.gestankbratwurst.core.mmcore.skinclient.mineskin.data.Skin;
import com.gestankbratwurst.core.mmcore.tablist.implementation.TabLine;
import com.gestankbratwurst.core.mmcore.util.common.NamespaceFactory;
import com.gestankbratwurst.core.mmcore.util.common.UtilItem;
import com.gestankbratwurst.core.mmcore.util.items.ItemBuilder;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import java.io.File;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 * This file is part of avarioncore and was created at the 24.11.2019
 * avarioncore can not be copied and/or distributed without the express
 * permission of the owner.
 */
public enum TextureModel {

  CLEAR_WATER_BOTTLE(Material.POTION, 1, false, false, false),
  MURKY_WATER_BOTTLE(Material.POTION, 2, false, false, false),
  SALT_WATER_BOTTLE(Material.POTION, 3, false, false, false),
  EMPTY_WATER_BOTTLE(Material.CLAY_BALL, 4, false, false, false),
  DUMMY_SWORD(Material.WOODEN_AXE, 5,false, true, false),

  BLACK_ARROW_DOWN(Material.STICK, 1000, false, false, false),
  BLACK_ARROW_LEFT(Material.STICK, 1001, false, false, false),
  BLACK_ARROW_RIGHT(Material.STICK, 1002, false, false, false),
  BLACK_ARROW_UP(Material.STICK, 1003, false, false, false),
  GREEN_CHECK(Material.STICK, 1004, false, false, false),
  RED_X(Material.STICK, 1005, false, false, false),
  RED_X_BOW(Material.BOW, 1005, false, false, false),
  RED_X_BREAD(Material.BREAD, 1005, false, false, false),
  DOUBLE_GRAY_ARROW_UP(Material.STICK, 1006, false, false, false),
  DOUBLE_GRAY_ARROW_DOWN(Material.STICK, 1007, false, false, false),
  DOUBLE_GRAY_ARROW_LEFT(Material.STICK, 1008, false, false, false),
  DOUBLE_GRAY_ARROW_RIGHT(Material.STICK, 1009, false, false, false),
  DOUBLE_RED_ARROW_UP(Material.STICK, 1010, false, false, false),
  DOUBLE_GREEN_ARROW_DOWN(Material.STICK, 1011, false, false, false),
  GREEN_PLUS(Material.STICK, 1012, false, false, false),
  SCROLL(Material.STICK, 1013, false, false, false),

  LETTER_NO(Material.STICK, 1014, false, false, false),
  LETTER_YES(Material.STICK, 1015, false, false, false),
  LOCK_OPEN(Material.STICK, 1016, false, false, false),
  LOCK_CLOSED(Material.STICK, 1017, false, false, false),

  HUMAN_ICON(Material.STICK, 1018, false, false, false),
  ORC_ICON(Material.STICK, 1019, false, false, false),
  DWARF_ICON(Material.STICK, 1020, false, false, false),
  ELF_ICON(Material.STICK, 1021, false, false, false),
  UNDEAD_ICON(Material.STICK, 1022, false, false, false),
  WAR_ICON(Material.STICK, 1023, false, false, false),
  PEACE_ICON(Material.STICK, 1024, false, false, false),
  BROWN_BOOK(Material.STICK, 1025, false, false, false),
  HORN_ICON(Material.STICK, 1026, false, false, false),
  ELF_ORB(Material.STICK, 1027, false, false, false),
  CEMENT_BALL(Material.STICK, 1028, false, false, false),
  HOLY_BOOK(Material.STICK, 1029, false, false, false),
  ROLE_ICON(Material.STICK, 1030, false, false, false),
  FLAME_ICON_EMPTY(Material.STICK, 1031, false, false, false, FontMeta.of(12, 9, "bitmap")),
  FLAME_ICON_HALF(Material.STICK, 1032, false, false, false, FontMeta.of(12, 9, "bitmap")),
  FLAME_ICON_FULL(Material.STICK, 1033, false, false, false, FontMeta.of(12, 9, "bitmap")),
  PRECISION_ICON(Material.STICK, 1034, false, false, false),
  HEALTH_ICON(Material.STICK, 1035, false, false, false),
  DAMAGE_ICON(Material.STICK, 1036, false, false, false),
  DEFENCE_ICON(Material.STICK, 1037, false, false, false),
  ATTRIBUTES_ICON(Material.STICK, 1038, false, false, false),
  BASE_UPGRADE_ICON(Material.STICK, 1039, false, false, false),
  BASE_UPGRADE_DENY_ICON(Material.STICK, 1040, false, false, false),
  EPRO_ICON(Material.STICK, 1041, false, false, false, FontMeta.of(64, 16, "bitmap")),
  EPRO_ICON_MC(Material.STICK, 1042, false, false, false, FontMeta.of(64, 16, "bitmap")),

  GOLD_PILE_TINY(Material.STICK, 1100, false, false, false),
  GOLD_PILE_SMALL(Material.STICK, 1101, false, false, false),
  GOLD_PILE_MEDIUM(Material.STICK, 1102, false, false, false),
  GOLD_PILE_BIG(Material.STICK, 1103, false, false, false),
  GOLD_PILE_HUGE(Material.STICK, 1104, false, false, false),
  GOLD_PILE_BAR_SMALL(Material.STICK, 1105, false, false, false),
  GOLD_PILE_BAR_MEDIUM(Material.STICK, 1106, false, false, false),
  GOLD_PILE_BAR_BIG(Material.STICK, 1107, false, false, false),
  SILVER_PILE_TINY(Material.STICK, 1108, false, false, false),
  SILVER_PILE_SMALL(Material.STICK, 1109, false, false, false),
  SILVER_PILE_MEDIUM(Material.STICK, 1110, false, false, false),
  SILVER_PILE_BIG(Material.STICK, 1111, false, false, false),
  SILVER_PILE_HUGE(Material.STICK, 1112, false, false, false),
  COPPER_PILE_TINY(Material.STICK, 1113, false, false, false),
  COPPER_PILE_SMALL(Material.STICK, 1114, false, false, false),
  COPPER_PILE_MEDIUM(Material.STICK, 1115, false, false, false),
  COPPER_PILE_BIG(Material.STICK, 1116, false, false, false),
  COPPER_PILE_HUGE(Material.STICK, 1117, false, false, false),
  COINS_GOLD_0(Material.STICK, 1118, false, false, false),
  COINS_GOLD_1(Material.STICK, 1119, false, false, false),
  COINS_GOLD_2(Material.STICK, 1120, false, false, false),
  COINS_GOLD_3(Material.STICK, 1121, false, false, false),
  COINS_GOLD_4(Material.STICK, 1122, false, false, false),
  COINS_GOLD_5(Material.STICK, 1123, false, false, false),
  COINS_SILVER_0(Material.STICK, 1124, false, false, false),
  COINS_SILVER_1(Material.STICK, 1125, false, false, false),
  COINS_SILVER_2(Material.STICK, 1126, false, false, false),
  COINS_SILVER_3(Material.STICK, 1127, false, false, false),
  COINS_SILVER_4(Material.STICK, 1128, false, false, false),
  COINS_SILVER_5(Material.STICK, 1129, false, false, false),
  COINS_COPPER_0(Material.STICK, 1130, false, false, false),
  COINS_COPPER_1(Material.STICK, 1131, false, false, false),
  COINS_COPPER_2(Material.STICK, 1132, false, false, false),
  COINS_COPPER_3(Material.STICK, 1133, false, false, false),
  COINS_COPPER_4(Material.STICK, 1134, false, false, false),
  COINS_COPPER_5(Material.STICK, 1135, false, false, false),
  BARS_GOLD_0(Material.STICK, 1136, false, false, false),
  BARS_GOLD_1(Material.STICK, 1137, false, false, false),
  BARS_GOLD_2(Material.STICK, 1138, false, false, false),
  KILL_QUEST_ICON(Material.STONE_SWORD, 1139, false, false, false),
  GATHER_QUEST_ICON(Material.STONE_SWORD, 1140, false, false, false),
  ACTION_BAR_ICON_MIDDLE(Material.STICK, 1141, false, false, false),
  ACTION_BAR_ICON_LEFT(Material.STICK, 1142, false, false, false),
  ACTION_BAR_ICON_RIGHT(Material.STICK, 1143, false, false, false),
  WOODEN_SPEAR_PIKE(Material.STICK, 1144, false, false, false),
  STONE_SPEAR_PIKE(Material.STICK, 1145, false, false, false),
  GOLDEN_SPEAR_PIKE(Material.STICK, 1146, false, false, false),
  IRON_SPEAR_PIKE(Material.STICK, 1147, false, false, false),
  DIAMOND_SPEAR_PIKE(Material.STICK, 1148, false, false, false),
  NETHERITE_SPEAR_PIKE(Material.STICK, 1149, false, false, false),
  SWORD_FIGHTER_ICON(Material.STICK, 1150, false, false, false),
  ALCHEMIST_ICON(Material.STICK, 1151, false, false, false),
  AXE_FIGHTER_ICON(Material.STICK, 1152, false, false, false),
  SPEAR_FIGHTER_ICON(Material.STICK, 1153, false, false, false),
  BOW_FIGHTER_ICON(Material.STICK, 1154, false, false, false),
  SHAMAN_ICON(Material.STICK, 1155, false, false, false),
  ALCHEMIST_STAFF_HEAD(Material.STICK, 1156, false, false, false),
  SHAMAN_STAFF_HEAD(Material.STICK, 1157, false, false, false),
  LOGO(Material.STICK, 1158, false, false, false, FontMeta.of(48, 48, "bitmap")),
  TORSO_ICON(Material.STICK, 1159, false, false, false),
  FEET_ICON(Material.STICK, 1160, false, false, false),
  HANDS_ICON(Material.STICK, 1161, false, false, false),
  HEART_ICON(Material.STICK, 1162, false, false, false),
  UP_DOWN_ICON(Material.STICK, 1163, false, false, false),
  DOUBLE_GREEN_ARROW_UP(Material.STICK, 1164, false, false, false),
  DOUBLE_RED_ARROW_DOWN(Material.STICK, 1165, false, false, false),
  BANDAGE_ICON(Material.STICK, 1166, false, false, false),
  STRENGTH_ICON(Material.STICK, 1167, false, false, false),
  INTELLECT_ICON(Material.STICK, 1168, false, false, false),
  PRESTIGE_ICON(Material.STICK, 1169, false, false, false),
  HEAD_SELECT(Material.STICK, 1170, false, false, false),
  BODY_SELECT(Material.STICK, 1171, false, false, false),
  LEFT_LEG_SELECT(Material.STICK, 1172, false, false, false),
  RIGHT_LEG_SELECT(Material.STICK, 1173, false, false, false),
  LEFT_ARM_SELECT(Material.STICK, 1174, false, false, false),
  RIGHT_ARM_SELECT(Material.STICK, 1175, false, false, false),
  X_ROLL(Material.STICK, 1176, false, false, false),
  Y_ROLL(Material.STICK, 1177, false, false, false),
  Z_ROLL(Material.STICK, 1178, false, false, false),

  DWARF_CANON(Material.STICK, 1200, false, true, false),
  HUMAN_CROWN(Material.STICK, 1201, false, true, false),
  DWARF_CROWN(Material.STICK, 1202, false, true, false),
  ELF_CROWN(Material.STICK, 1203, false, true, false),
  UNDEAD_CROWN(Material.STICK, 1204, false, true, false),
  CANON_BALL(Material.STICK, 1205, false, true, false),
  HEALING_FOUNTAIN(Material.STICK, 1206, false, true, false),
  ELF_ORB_MODEL(Material.STICK, 1207, false, true, false),
  UNDEAD_TOTEM(Material.STICK, 1208, false, true, false),
  ORC_HORN(Material.STICK, 1209, false, true, false),
  HOLY_CIRCLE(Material.STICK, 1210, false, true, false),
  WOODEN_SPEAR(Material.WOODEN_SWORD, 1211, false, true, false),
  STONE_SPEAR(Material.STONE_SWORD, 1212, false, true, false),
  GOLDEN_SPEAR(Material.GOLDEN_SWORD, 1213, false, true, false),
  IRON_SPEAR(Material.IRON_SWORD, 1214, false, true, false),
  DIAMOND_SPEAR(Material.DIAMOND_SWORD, 1215, false, true, false),
  NETHERITE_SPEAR(Material.NETHERITE_SWORD, 1216, false, true, false),
  SHAMAN_STAFF(Material.STONE_SWORD, 1217, false, true, false),
  ALCHEMIST_STAFF(Material.STONE_SWORD, 1218, false, true, false),
  ORC_CROWN(Material.STICK, 1219, false, true, false),
  LOW_QUALITY_MAP(Material.STICK, 1220, false, true, false),
  HIGH_QUALITY_MAP(Material.STICK, 1221, false, true, false),
  BRICK_HAMMER(Material.IRON_PICKAXE, 1222, false, true, false),
  PRIMITIVE_KNIFE(Material.IRON_AXE, 1223, false, false, false),
  EPRO_DEX(Material.STICK, 1224, false, false, false),
  BLOCK_DEX_ICON(Material.IRON_HOE, 1225, false, false, false),
  KILL_DEX_ICON(Material.IRON_HOE, 1226, false, false, false),
  BIOME_DEX_ICON(Material.IRON_HOE, 1227, false, false, false),
  EMOJI_01(Material.STICK, 1228, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_02(Material.STICK, 1229, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_03(Material.STICK, 1230, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_04(Material.STICK, 1231, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_05(Material.STICK, 1232, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_06(Material.STICK, 1233, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_07(Material.STICK, 1234, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_08(Material.STICK, 1235, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_09(Material.STICK, 1236, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_10(Material.STICK, 1237, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_11(Material.STICK, 1238, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_12(Material.STICK, 1239, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_13(Material.STICK, 1240, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_14(Material.STICK, 1241, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_15(Material.STICK, 1242, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_16(Material.STICK, 1243, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_17(Material.STICK, 1244, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_18(Material.STICK, 1245, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_19(Material.STICK, 1246, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_20(Material.STICK, 1247, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_21(Material.STICK, 1248, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_22(Material.STICK, 1249, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_23(Material.STICK, 1250, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_24(Material.STICK, 1251, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_25(Material.STICK, 1252, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_26(Material.STICK, 1253, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_27(Material.STICK, 1254, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_28(Material.STICK, 1255, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_29(Material.STICK, 1256, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_30(Material.STICK, 1257, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_31(Material.STICK, 1258, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_32(Material.STICK, 1259, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_33(Material.STICK, 1260, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_34(Material.STICK, 1261, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_35(Material.STICK, 1262, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_36(Material.STICK, 1263, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_37(Material.STICK, 1264, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_38(Material.STICK, 1265, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_39(Material.STICK, 1266, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_40(Material.STICK, 1267, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_41(Material.STICK, 1268, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_42(Material.STICK, 1269, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_43(Material.STICK, 1270, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_44(Material.STICK, 1271, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_45(Material.STICK, 1272, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_46(Material.STICK, 1273, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_47(Material.STICK, 1274, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_48(Material.STICK, 1275, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_49(Material.STICK, 1276, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_50(Material.STICK, 1277, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_51(Material.STICK, 1278, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_52(Material.STICK, 1279, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_53(Material.STICK, 1280, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_54(Material.STICK, 1281, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_55(Material.STICK, 1282, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_56(Material.STICK, 1283, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_57(Material.STICK, 1284, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_58(Material.STICK, 1285, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_59(Material.STICK, 1286, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_60(Material.STICK, 1287, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_61(Material.STICK, 1288, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_62(Material.STICK, 1289, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_63(Material.STICK, 1290, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_64(Material.STICK, 1291, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_65(Material.STICK, 1292, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_66(Material.STICK, 1293, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_67(Material.STICK, 1294, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_68(Material.STICK, 1295, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_69(Material.STICK, 1296, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_70(Material.STICK, 1297, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_71(Material.STICK, 1298, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_72(Material.STICK, 1299, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_73(Material.STICK, 1300, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_74(Material.STICK, 1301, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_75(Material.STICK, 1302, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_76(Material.STICK, 1303, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_77(Material.STICK, 1304, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_78(Material.STICK, 1305, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_79(Material.STICK, 1306, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_80(Material.STICK, 1307, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_81(Material.STICK, 1308, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_82(Material.STICK, 1309, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_83(Material.STICK, 1310, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_84(Material.STICK, 1311, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_85(Material.STICK, 1312, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_86(Material.STICK, 1313, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_87(Material.STICK, 1314, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_88(Material.STICK, 1315, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_89(Material.STICK, 1316, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_90(Material.STICK, 1317, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_91(Material.STICK, 1318, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_92(Material.STICK, 1319, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_93(Material.STICK, 1320, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_94(Material.STICK, 1321, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  EMOJI_95(Material.STICK, 1322, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  HEART_A(Material.STICK, 1323, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  HEART_K(Material.STICK, 1324, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  HEART_Q(Material.STICK, 1325, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  HEART_J(Material.STICK, 1326, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  HEART_10(Material.STICK, 1327, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  HEART_9(Material.STICK, 1328, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  HEART_8(Material.STICK, 1329, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  HEART_7(Material.STICK, 1330, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  HEART_6(Material.STICK, 1331, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  HEART_5(Material.STICK, 1332, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  HEART_4(Material.STICK, 1333, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  HEART_3(Material.STICK, 1334, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  HEART_2(Material.STICK, 1335, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  DIAMOND_A(Material.STICK, 1336, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  DIAMOND_K(Material.STICK, 1337, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  DIAMOND_Q(Material.STICK, 1338, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  DIAMOND_J(Material.STICK, 1339, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  DIAMOND_10(Material.STICK, 1340, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  DIAMOND_9(Material.STICK, 1341, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  DIAMOND_8(Material.STICK, 1342, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  DIAMOND_7(Material.STICK, 1343, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  DIAMOND_6(Material.STICK, 1344, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  DIAMOND_5(Material.STICK, 1345, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  DIAMOND_4(Material.STICK, 1346, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  DIAMOND_3(Material.STICK, 1347, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  DIAMOND_2(Material.STICK, 1348, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  KLUB_A(Material.STICK, 1349, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  KLUB_K(Material.STICK, 1350, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  KLUB_Q(Material.STICK, 1351, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  KLUB_J(Material.STICK, 1352, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  KLUB_10(Material.STICK, 1353, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  KLUB_9(Material.STICK, 1354, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  KLUB_8(Material.STICK, 1355, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  KLUB_7(Material.STICK, 1356, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  KLUB_6(Material.STICK, 1357, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  KLUB_5(Material.STICK, 1358, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  KLUB_4(Material.STICK, 1359, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  KLUB_3(Material.STICK, 1360, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  KLUB_2(Material.STICK, 1361, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  SPADE_A(Material.STICK, 1362, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  SPADE_K(Material.STICK, 1363, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  SPADE_Q(Material.STICK, 1364, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  SPADE_J(Material.STICK, 1365, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  SPADE_10(Material.STICK, 1366, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  SPADE_9(Material.STICK, 1367, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  SPADE_8(Material.STICK, 1368, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  SPADE_7(Material.STICK, 1369, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  SPADE_6(Material.STICK, 1370, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  SPADE_5(Material.STICK, 1371, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  SPADE_4(Material.STICK, 1372, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  SPADE_3(Material.STICK, 1373, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  SPADE_2(Material.STICK, 1374, false, false, false, new FontMeta(0, 0, 11, 9, "bitmap")),
  BLOCKED_HAND_ICON(Material.STICK, 1375, false, true, false),

  TRADE_GUI(Material.STICK, 2000, false, true, false),
  ADMIN_SHOP_BORDER(Material.STICK, 2001, false, true, false),
  RECIPE_VIEW_UI(Material.STICK, 2002, false, true, false),
  MOCK_UI(Material.STICK, 2003, false, false, false, new FontMeta(-100, -8, 90, 0, "bitmap")),
  DUMMY_UI(Material.STICK, 2004, false, false, false, new FontMeta(-100, -8, 256, 50, "bitmap")),
  PIXEL(Material.STICK, 2005, false, false, false, new FontMeta( 0, 0, -51, -2000, "bitmap")),
  DEFAULT_CRAFTING_UI(Material.STICK, 2006, false, false, false, new FontMeta(-100, -8, 256, 50, "bitmap")),

  DWARF_SKIN_1(Material.STICK, 2500, false, false, true),
  NETHERITE_BLOCK_SKIN(Material.STICK, 2501, false, false, true),
  COLD_RESIST(Material.STICK, 2502, false, false, false),
  HEAT_RESIST(Material.STICK, 2503, false, false, false),

  HEALTH_ICON_100(Material.STICK, 3000, false, false, false, iconFont()),
  HEALTH_ICON_90(Material.STICK, 3001, false, false, false, iconFont()),
  HEALTH_ICON_80(Material.STICK, 3002, false, false, false, iconFont()),
  HEALTH_ICON_70(Material.STICK, 3003, false, false, false, iconFont()),
  HEALTH_ICON_60(Material.STICK, 3004, false, false, false, iconFont()),
  HEALTH_ICON_50(Material.STICK, 3005, false, false, false, iconFont()),
  HEALTH_ICON_40(Material.STICK, 3006, false, false, false, iconFont()),
  HEALTH_ICON_30(Material.STICK, 3007, false, false, false, iconFont()),
  HEALTH_ICON_20(Material.STICK, 3008, false, false, false, iconFont()),
  HEALTH_ICON_10(Material.STICK, 3009, false, false, false, iconFont()),
  HEALTH_ICON_0(Material.STICK, 3010, false, false, false, iconFont()),

  BLOOD_ICON_100(Material.STICK, 3011, false, false, false, iconFont()),
  BLOOD_ICON_90(Material.STICK, 3012, false, false, false, iconFont()),
  BLOOD_ICON_80(Material.STICK, 3013, false, false, false, iconFont()),
  BLOOD_ICON_70(Material.STICK, 3014, false, false, false, iconFont()),
  BLOOD_ICON_60(Material.STICK, 3015, false, false, false, iconFont()),
  BLOOD_ICON_50(Material.STICK, 3016, false, false, false, iconFont()),
  BLOOD_ICON_40(Material.STICK, 3017, false, false, false, iconFont()),
  BLOOD_ICON_30(Material.STICK, 3018, false, false, false, iconFont()),
  BLOOD_ICON_20(Material.STICK, 3019, false, false, false, iconFont()),
  BLOOD_ICON_10(Material.STICK, 3020, false, false, false, iconFont()),
  BLOOD_ICON_0(Material.STICK, 3021, false, false, false, iconFont()),

  ENERGY_ICON_100(Material.STICK, 3022, false, false, false, iconFont()),
  ENERGY_ICON_90(Material.STICK, 3023, false, false, false, iconFont()),
  ENERGY_ICON_80(Material.STICK, 3024, false, false, false, iconFont()),
  ENERGY_ICON_70(Material.STICK, 3025, false, false, false, iconFont()),
  ENERGY_ICON_60(Material.STICK, 3026, false, false, false, iconFont()),
  ENERGY_ICON_50(Material.STICK, 3027, false, false, false, iconFont()),
  ENERGY_ICON_40(Material.STICK, 3028, false, false, false, iconFont()),
  ENERGY_ICON_30(Material.STICK, 3029, false, false, false, iconFont()),
  ENERGY_ICON_20(Material.STICK, 3030, false, false, false, iconFont()),
  ENERGY_ICON_10(Material.STICK, 3031, false, false, false, iconFont()),
  ENERGY_ICON_0(Material.STICK, 3032, false, false, false, iconFont()),

  WATER_ICON_100(Material.STICK, 3033, false, false, false, iconFont()),
  WATER_ICON_90(Material.STICK, 3034, false, false, false, iconFont()),
  WATER_ICON_80(Material.STICK, 3035, false, false, false, iconFont()),
  WATER_ICON_70(Material.STICK, 3036, false, false, false, iconFont()),
  WATER_ICON_60(Material.STICK, 3037, false, false, false, iconFont()),
  WATER_ICON_50(Material.STICK, 3038, false, false, false, iconFont()),
  WATER_ICON_40(Material.STICK, 3039, false, false, false, iconFont()),
  WATER_ICON_30(Material.STICK, 3040, false, false, false, iconFont()),
  WATER_ICON_20(Material.STICK, 3041, false, false, false, iconFont()),
  WATER_ICON_10(Material.STICK, 3042, false, false, false, iconFont()),
  WATER_ICON_0(Material.STICK, 3043, false, false, false, iconFont()),

  GRADIENT_ICON_3(Material.STICK, 3044, false, false, false, gradientFont()),
  GRADIENT_ICON_2(Material.STICK, 3045, false, false, false, gradientFont()),
  GRADIENT_ICON_1(Material.STICK, 3046, false, false, false, gradientFont()),
  GRADIENT_ICON_0(Material.STICK, 3047, false, false, false, gradientFont()),
  GRADIENT_ICON_M1(Material.STICK, 3048, false, false, false, gradientFont()),
  GRADIENT_ICON_M2(Material.STICK, 3049, false, false, false, gradientFont()),
  GRADIENT_ICON_M3(Material.STICK, 3050, false, false, false, gradientFont()),

  HEALTH_0(Material.STICK, 4000, false, false, false, iconFont()),
  HEALTH_4(Material.STICK, 4001, false, false, false, iconFont()),
  HEALTH_8(Material.STICK, 4002, false, false, false, iconFont()),
  HEALTH_12(Material.STICK, 4003, false, false, false, iconFont()),
  HEALTH_16(Material.STICK, 4004, false, false, false, iconFont()),
  HEALTH_20(Material.STICK, 4005, false, false, false, iconFont()),
  HEALTH_24(Material.STICK, 4006, false, false, false, iconFont()),
  HEALTH_28(Material.STICK, 4007, false, false, false, iconFont()),
  HEALTH_32(Material.STICK, 4008, false, false, false, iconFont()),
  HEALTH_36(Material.STICK, 4009, false, false, false, iconFont()),
  HEALTH_40(Material.STICK, 4010, false, false, false, iconFont()),
  HEALTH_44(Material.STICK, 4011, false, false, false, iconFont()),
  HEALTH_48(Material.STICK, 4012, false, false, false, iconFont()),
  HEALTH_52(Material.STICK, 4013, false, false, false, iconFont()),
  HEALTH_56(Material.STICK, 4014, false, false, false, iconFont()),
  HEALTH_60(Material.STICK, 4015, false, false, false, iconFont()),
  HEALTH_64(Material.STICK, 4016, false, false, false, iconFont()),
  HEALTH_68(Material.STICK, 4017, false, false, false, iconFont()),
  HEALTH_72(Material.STICK, 4018, false, false, false, iconFont()),
  HEALTH_76(Material.STICK, 4019, false, false, false, iconFont()),
  HEALTH_80(Material.STICK, 4020, false, false, false, iconFont()),
  HEALTH_84(Material.STICK, 4021, false, false, false, iconFont()),
  HEALTH_88(Material.STICK, 4022, false, false, false, iconFont()),
  HEALTH_92(Material.STICK, 4023, false, false, false, iconFont()),
  HEALTH_96(Material.STICK, 4024, false, false, false, iconFont()),
  HEALTH_100(Material.STICK, 4025, false, false, false, iconFont()),

  WATER_0(Material.STICK, 4026, false, false, false, iconFont()),
  WATER_4(Material.STICK, 4027, false, false, false, iconFont()),
  WATER_8(Material.STICK, 4028, false, false, false, iconFont()),
  WATER_12(Material.STICK, 4029, false, false, false, iconFont()),
  WATER_16(Material.STICK, 4030, false, false, false, iconFont()),
  WATER_20(Material.STICK, 4031, false, false, false, iconFont()),
  WATER_24(Material.STICK, 4032, false, false, false, iconFont()),
  WATER_28(Material.STICK, 4033, false, false, false, iconFont()),
  WATER_32(Material.STICK, 4034, false, false, false, iconFont()),
  WATER_36(Material.STICK, 4035, false, false, false, iconFont()),
  WATER_40(Material.STICK, 4036, false, false, false, iconFont()),
  WATER_44(Material.STICK, 4037, false, false, false, iconFont()),
  WATER_48(Material.STICK, 4038, false, false, false, iconFont()),
  WATER_52(Material.STICK, 4039, false, false, false, iconFont()),
  WATER_56(Material.STICK, 4040, false, false, false, iconFont()),
  WATER_60(Material.STICK, 4041, false, false, false, iconFont()),
  WATER_64(Material.STICK, 4042, false, false, false, iconFont()),
  WATER_68(Material.STICK, 4043, false, false, false, iconFont()),
  WATER_72(Material.STICK, 4044, false, false, false, iconFont()),
  WATER_76(Material.STICK, 4045, false, false, false, iconFont()),
  WATER_80(Material.STICK, 4046, false, false, false, iconFont()),
  WATER_84(Material.STICK, 4047, false, false, false, iconFont()),
  WATER_88(Material.STICK, 4048, false, false, false, iconFont()),
  WATER_92(Material.STICK, 4049, false, false, false, iconFont()),
  WATER_96(Material.STICK, 4050, false, false, false, iconFont()),
  WATER_100(Material.STICK, 4051, false, false, false, iconFont()),

  NUTRITION_0(Material.STICK, 4052, false, false, false, iconFont()),
  NUTRITION_4(Material.STICK, 4053, false, false, false, iconFont()),
  NUTRITION_8(Material.STICK, 4054, false, false, false, iconFont()),
  NUTRITION_12(Material.STICK, 4055, false, false, false, iconFont()),
  NUTRITION_16(Material.STICK, 4056, false, false, false, iconFont()),
  NUTRITION_20(Material.STICK, 4057, false, false, false, iconFont()),
  NUTRITION_24(Material.STICK, 4058, false, false, false, iconFont()),
  NUTRITION_28(Material.STICK, 4059, false, false, false, iconFont()),
  NUTRITION_32(Material.STICK, 4060, false, false, false, iconFont()),
  NUTRITION_36(Material.STICK, 4061, false, false, false, iconFont()),
  NUTRITION_40(Material.STICK, 4062, false, false, false, iconFont()),
  NUTRITION_44(Material.STICK, 4063, false, false, false, iconFont()),
  NUTRITION_48(Material.STICK, 4064, false, false, false, iconFont()),
  NUTRITION_52(Material.STICK, 4065, false, false, false, iconFont()),
  NUTRITION_56(Material.STICK, 4066, false, false, false, iconFont()),
  NUTRITION_60(Material.STICK, 4067, false, false, false, iconFont()),
  NUTRITION_64(Material.STICK, 4068, false, false, false, iconFont()),
  NUTRITION_68(Material.STICK, 4069, false, false, false, iconFont()),
  NUTRITION_72(Material.STICK, 4070, false, false, false, iconFont()),
  NUTRITION_76(Material.STICK, 4071, false, false, false, iconFont()),
  NUTRITION_80(Material.STICK, 4072, false, false, false, iconFont()),
  NUTRITION_84(Material.STICK, 4073, false, false, false, iconFont()),
  NUTRITION_88(Material.STICK, 4074, false, false, false, iconFont()),
  NUTRITION_92(Material.STICK, 4075, false, false, false, iconFont()),
  NUTRITION_96(Material.STICK, 4076, false, false, false, iconFont()),
  NUTRITION_100(Material.STICK, 4078, false, false, false, iconFont()),

  WEIGHT_0(Material.STICK, 4079, false, false, false, iconFont()),
  WEIGHT_4(Material.STICK, 4080, false, false, false, iconFont()),
  WEIGHT_8(Material.STICK, 4081, false, false, false, iconFont()),
  WEIGHT_12(Material.STICK, 4082, false, false, false, iconFont()),
  WEIGHT_16(Material.STICK, 4083, false, false, false, iconFont()),
  WEIGHT_20(Material.STICK, 4084, false, false, false, iconFont()),
  WEIGHT_24(Material.STICK, 4085, false, false, false, iconFont()),
  WEIGHT_28(Material.STICK, 4086, false, false, false, iconFont()),
  WEIGHT_32(Material.STICK, 4087, false, false, false, iconFont()),
  WEIGHT_36(Material.STICK, 4088, false, false, false, iconFont()),
  WEIGHT_40(Material.STICK, 4089, false, false, false, iconFont()),
  WEIGHT_44(Material.STICK, 4090, false, false, false, iconFont()),
  WEIGHT_48(Material.STICK, 4091, false, false, false, iconFont()),
  WEIGHT_52(Material.STICK, 4092, false, false, false, iconFont()),
  WEIGHT_56(Material.STICK, 4093, false, false, false, iconFont()),
  WEIGHT_60(Material.STICK, 4094, false, false, false, iconFont()),
  WEIGHT_64(Material.STICK, 4095, false, false, false, iconFont()),
  WEIGHT_68(Material.STICK, 4096, false, false, false, iconFont()),
  WEIGHT_72(Material.STICK, 4097, false, false, false, iconFont()),
  WEIGHT_76(Material.STICK, 4098, false, false, false, iconFont()),
  WEIGHT_80(Material.STICK, 4099, false, false, false, iconFont()),
  WEIGHT_84(Material.STICK, 4100, false, false, false, iconFont()),
  WEIGHT_88(Material.STICK, 4101, false, false, false, iconFont()),
  WEIGHT_92(Material.STICK, 4102, false, false, false, iconFont()),
  WEIGHT_96(Material.STICK, 4103, false, false, false, iconFont()),
  WEIGHT_100(Material.STICK, 4104, false, false, false, iconFont()),

  THERMOMETER_0(Material.STICK, 4105, false, false, false, FontMeta.of(28,3, "bitmap")),
  THERMOMETER_9(Material.STICK, 4106, false, false, false, FontMeta.of(28,3, "bitmap")),
  THERMOMETER_16(Material.STICK, 4107, false, false, false, FontMeta.of(28,3, "bitmap")),
  THERMOMETER_23(Material.STICK, 4108, false, false, false, FontMeta.of(28,3, "bitmap")),
  THERMOMETER_30(Material.STICK, 4109, false, false, false, FontMeta.of(28,3, "bitmap")),
  THERMOMETER_37(Material.STICK, 4110, false, false, false, FontMeta.of(28,3, "bitmap")),
  THERMOMETER_44(Material.STICK, 4111, false, false, false, FontMeta.of(28,3, "bitmap")),
  THERMOMETER_51(Material.STICK, 4112, false, false, false, FontMeta.of(28,3, "bitmap")),
  THERMOMETER_58(Material.STICK, 4113, false, false, false, FontMeta.of(28,3, "bitmap")),
  THERMOMETER_65(Material.STICK, 4114, false, false, false, FontMeta.of(28,3, "bitmap")),
  THERMOMETER_72(Material.STICK, 4115, false, false, false, FontMeta.of(28,3, "bitmap")),
  THERMOMETER_79(Material.STICK, 4116, false, false, false, FontMeta.of(28,3, "bitmap")),
  THERMOMETER_86(Material.STICK, 4117, false, false, false, FontMeta.of(28,3, "bitmap")),
  THERMOMETER_93(Material.STICK, 4118, false, false, false, FontMeta.of(28,3, "bitmap")),
  THERMOMETER_100(Material.STICK, 4119, false, false, false, FontMeta.of(28,3, "bitmap")),

  WET_EFFECT(Material.NETHER_BRICK, 1, false, false, false, FontMeta.of(18,0, "bitmap")),
  WET_EFFECT_SMALL(Material.NETHER_BRICK, 2, false, false, false),
  DRY_EFFECT(Material.NETHER_BRICK, 3, false, false, false, FontMeta.of(18,0, "bitmap")),
  DRY_EFFECT_SMALL(Material.NETHER_BRICK, 4, false, false, false),
  HUNGER_DEBUFF(Material.NETHER_BRICK, 5, false, false, false, FontMeta.of(18,0, "bitmap")),
  HUNGER_DEBUFF_SMALL(Material.NETHER_BRICK, 6, false, false, false),
  THIRST_DEBUFF(Material.NETHER_BRICK, 7, false, false, false, FontMeta.of(18,0, "bitmap")),
  THIRST_DEBUFF_SMALL(Material.NETHER_BRICK, 8, false, false, false),
  OVERWEIGHT_DEBUFF_I(Material.NETHER_BRICK, 9, false, false, false, FontMeta.of(18,0, "bitmap")),
  OVERWEIGHT_DEBUFF_I_SMALL(Material.NETHER_BRICK, 10, false, false, false),
  OVERWEIGHT_DEBUFF_II(Material.NETHER_BRICK, 11, false, false, false, FontMeta.of(18,0, "bitmap")),
  OVERWEIGHT_DEBUFF_II_SMALL(Material.NETHER_BRICK, 12, false, false, false),
  CAMPFIRE_BUFF(Material.NETHER_BRICK, 13, false, false, false, FontMeta.of(18,0, "bitmap")),
  CAMPFIRE_BUFF_SMALL(Material.NETHER_BRICK, 14, false, false, false),
  OVERHEAT_DEBUFF(Material.NETHER_BRICK, 15, false, false, false, FontMeta.of(18,0, "bitmap")),
  OVERHEAT_DEBUFF_SMALL(Material.NETHER_BRICK, 16, false, false, false),
  UNDERCOOL_DEBUFF(Material.NETHER_BRICK, 17, false, false, false, FontMeta.of(18,0, "bitmap")),
  UNDERCOOL_DEBUFF_SMALL(Material.NETHER_BRICK, 18, false, false, false),
  SALT_POISONING(Material.NETHER_BRICK, 19, false, false, false, FontMeta.of(18,0, "bitmap")),
  SALT_POISONING_SMALL(Material.NETHER_BRICK, 20, false, false, false),
  FOOD_POISONING(Material.NETHER_BRICK, 21, false, false, false, FontMeta.of(18,0, "bitmap")),
  FOOD_POISONING_SMALL(Material.NETHER_BRICK, 22, false, false, false),
  BROKEN_BONE(Material.NETHER_BRICK, 23, false, false, false, FontMeta.of(18,0, "bitmap")),
  BROKEN_BONE_SMALL(Material.NETHER_BRICK, 24, false, false, false),
  HEAD_BROKEN_BONE(Material.NETHER_BRICK, 25, false, false, false, FontMeta.of(18,0, "bitmap")),
  HEAD_BROKEN_BONE_SMALL(Material.NETHER_BRICK, 26, false, false, false),
  TORSO_BROKEN_BONE(Material.NETHER_BRICK, 27, false, false, false, FontMeta.of(18,0, "bitmap")),
  TORSO_BROKEN_BONE_SMALL(Material.NETHER_BRICK, 28, false, false, false),
  HEALING_BONE(Material.NETHER_BRICK, 29, false, false, false, FontMeta.of(18,0, "bitmap")),
  HEALING_BONE_SMALL(Material.NETHER_BRICK, 30, false, false, false),
  BONE(Material.NETHER_BRICK, 31, false, false, false),
  HEAD_BONE(Material.NETHER_BRICK, 32, false, false, false),
  TORSO_BONE(Material.NETHER_BRICK, 33, false, false, false),
  BLEEDING_1(Material.NETHER_BRICK, 34, false, false, false, FontMeta.of(18,0, "bitmap")),
  BLEEDING_1_SMALL(Material.NETHER_BRICK, 35, false, false, false),
  BLEEDING_2(Material.NETHER_BRICK, 36, false, false, false, FontMeta.of(18,0, "bitmap")),
  BLEEDING_2_SMALL(Material.NETHER_BRICK, 37, false, false, false),
  BLEEDING_3(Material.NETHER_BRICK, 38, false, false, false, FontMeta.of(18,0, "bitmap")),
  BLEEDING_3_SMALL(Material.NETHER_BRICK, 39, false, false, false),
  BLEEDING_4(Material.NETHER_BRICK, 40, false, false, false, FontMeta.of(18,0, "bitmap")),
  BLEEDING_4_SMALL(Material.NETHER_BRICK, 41, false, false, false),
  BLEEDING_5(Material.NETHER_BRICK, 42, false, false, false, FontMeta.of(18,0, "bitmap")),
  BLEEDING_5_SMALL(Material.NETHER_BRICK, 43, false, false, false),
  WOUND_INFECTION(Material.NETHER_BRICK, 44, false, false, false, FontMeta.of(18,0, "bitmap")),
  WOUND_INFECTION_SMALL(Material.NETHER_BRICK, 45, false, false, false),
  BIO_HAZARD(Material.NETHER_BRICK, 46, false, false, false, FontMeta.of(18,0, "bitmap")),
  BIO_HAZARD_SMALL(Material.NETHER_BRICK, 47, false, false, false),
  HEALING_HEAD_BONE(Material.NETHER_BRICK, 48, false, false, false, FontMeta.of(18,0, "bitmap")),
  TORSO_HEALING_BONE(Material.NETHER_BRICK, 49, false, false, false, FontMeta.of(18,0, "bitmap")),
  HEALING_HEAD_BONE_SMALL(Material.NETHER_BRICK, 50, false, false, false),
  TORSO_HEALING_BONE_SMALL(Material.NETHER_BRICK, 51, false, false, false),
  GREEN_HEAL_CROSS(Material.NETHER_BRICK, 52, false, false, false, FontMeta.of(18,0, "bitmap")),
  GREEN_HEAL_CROSS_SMALL(Material.NETHER_BRICK, 53, false, false, false),
  SOUND_ICON(Material.NETHER_BRICK, 54, false, false, false, FontMeta.of(18,0, "bitmap")),
  SOUND_ICON_SMALL(Material.NETHER_BRICK, 55, false, false, false),

  TAB_ICON_SERVER(Material.ACACIA_BOAT, 1, false, false, false, tabFont()),
  TAB_ICON_BODY(Material.ACACIA_BOAT, 2, false, false, false, tabFont()),
  TAB_ICON_STATS(Material.ACACIA_BOAT, 3, false, false, false, tabFont()),
  TAB_ICON_EFFECTS(Material.ACACIA_BOAT, 4, false, false, false, tabFont()),
  REVENANT_TITLE(Material.ACACIA_BOAT, 5, false, false, false, FontMeta.of(128, 50, "bitmap"));

  TextureModel(final Material baseMaterial, final int modelID, final boolean headEnabled, final boolean customModelDataEnabled,
      final boolean playerSkinModel) {
    this.baseMaterial = baseMaterial;
    this.modelID = modelID;
    this.modelData = ModelData.defaultGenerated();
    this.fontMeta = FontMeta.common();
    this.boxedFontChar = new BoxedFontChar();
    this.headSkinEnabled = playerSkinModel || headEnabled;
    this.customModelDataEnabled = customModelDataEnabled;
    this.playerSkinModel = playerSkinModel;
  }

  TextureModel(final Material baseMaterial, final int modelID, final boolean headEnabled, final boolean customModelDataEnabled,
      final boolean playerSkinModel, final FontMeta fontMeta) {
    this.baseMaterial = baseMaterial;
    this.modelID = modelID;
    this.modelData = ModelData.defaultGenerated();
    this.fontMeta = fontMeta;
    this.boxedFontChar = new BoxedFontChar();
    this.headSkinEnabled = playerSkinModel || headEnabled;
    this.customModelDataEnabled = customModelDataEnabled;
    this.playerSkinModel = playerSkinModel;
  }

  TextureModel(final Material baseMaterial, final int modelID, final ModelData modelData, final FontMeta fontMeta,
      final boolean headEnabled,
      final boolean customModelDataEnabled, final boolean playerSkinModel) {
    Bukkit.getServer().getName();
    this.baseMaterial = baseMaterial;
    this.modelID = modelID;
    this.modelData = modelData;
    this.fontMeta = fontMeta;
    this.boxedFontChar = new BoxedFontChar();
    this.headSkinEnabled = headEnabled;
    this.customModelDataEnabled = customModelDataEnabled;
    this.playerSkinModel = playerSkinModel;
  }

  private static FontMeta iconFont() {
    return new FontMeta(20, 0, 38, 12, "bitmap");
  }

  private static FontMeta tabFont() {
    return FontMeta.of(52, 28, "bitmap");
  }

  private static FontMeta gradientFont() {
    return new FontMeta(0, 0, 42, 4, "bitmap");
  }

  @Getter
  private final Material baseMaterial;
  @Getter
  private final int modelID;
  @Getter
  private final ModelData modelData;
  @Getter
  private final FontMeta fontMeta;
  @Getter
  private final BoxedFontChar boxedFontChar;
  @Getter
  private final boolean headSkinEnabled;
  @Getter
  private final boolean customModelDataEnabled;
  @Getter
  private final boolean playerSkinModel;
  @Getter
  @Setter
  private Skin skin;
  @Getter
  @Setter
  private File linkedImageFile;
  @Getter
  private GameProfile gameProfile;

  private ItemStack head;

  private ItemStack item;

  private void initProfile() {
    if (this.gameProfile == null && this.skin != null) {
      this.gameProfile = new GameProfile(this.skin.data.uuid, this.skin.name);
      this.gameProfile.getProperties()
          .put("textures", new Property("textures", this.skin.data.texture.value, this.skin.data.texture.signature));
    }
  }

  public void applySkinTo(final Player player) {
    final PlayerProfile profile = player.getPlayerProfile();
    profile.removeProperty("textures");
    profile.setProperty(new ProfileProperty("textures", this.skin.data.texture.value, this.skin.data.texture.signature));
    player.setPlayerProfile(profile);
  }

  public static TextureModel ofItemStack(ItemStack itemStack) {
    if(itemStack == null) {
      return null;
    }
    ItemMeta meta = itemStack.getItemMeta();
    if(meta == null) {
      return null;
    }
    PersistentDataContainer container = meta.getPersistentDataContainer();
    String modelId = container.get(Objects.requireNonNull(NamespacedKey.fromString("Model")), PersistentDataType.STRING);
    if(modelId == null) {
      return null;
    }
    return TextureModel.valueOf(modelId);
  }

  public void applySkinTo(final TabLine line) {
    line.setTexture(this.skin.data.texture.value, this.skin.data.texture.signature);
  }

  public char getChar() {
    return this.boxedFontChar.getAsCharacter();
  }

  public ItemStack getItem() {
    if (this.item == null) {
      this.item = new ItemBuilder(this.baseMaterial)
          .modelData(this.modelID)
          .name(this.toString())
          .addPersistentData("Model", PersistentDataType.STRING, this.toString())
          .build();
    }
    return this.item.clone();
  }

  public ItemStack getHead() {
    if (this.head != null) {
      return this.head.clone();
    }
    this.initProfile();

    this.head = UtilItem.produceHead(this.gameProfile);

    final ItemMeta meta = this.head.getItemMeta();
    final PersistentDataContainer container = meta.getPersistentDataContainer();
    container.set(NamespaceFactory.provide("Model"), PersistentDataType.STRING, this.toString());
    this.head.setItemMeta(meta);
    return this.head.clone();
  }

}