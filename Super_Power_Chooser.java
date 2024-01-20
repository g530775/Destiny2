package Destiny2;

import Destiny2.Misc.Misc;
import Destiny2.Misc.Summon;
import Destiny2.Misc.Test_Listener;
import Destiny2.Super_Power.Well_Of_Radiance_Listener;
import Destiny2.Variable_Construction.Exp;
import Destiny2.Variable_Construction.Position;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.bukkit.inventory.meta.Damageable;

import java.util.LinkedList;
import java.util.List;


public class Super_Power_Chooser implements Listener{
    @EventHandler
    public void Listener(PlayerToggleSneakEvent ptse){
        Player p=ptse.getPlayer();
        ItemStack i = p.getInventory().getItemInMainHand();
        Material m=i.getType();
//        if(m== Material.IRON_INGOT &&ptse.isSneaking()){
//            Block B=p.getTargetBlockExact(1);
//            Bukkit.broadcastMessage(B.toString());
//        }
//        if(m== Material.GOLD_INGOT&&ptse.isSneaking()){
//
//        }
        if(m== Material.END_CRYSTAL&&ptse.isSneaking()){
            switch (i.getItemMeta().getDisplayName()) {
                case "雷霆冲击" -> Thunder_Crash(p);
                case "光焰之井" -> Well_Of_Radiance(p);
                case "狩猎陷阱" -> Shadow_Shot(p, false);
                case "莫比乌斯箭袋" -> Shadow_Shot(p, true);
                case "精准黄金枪" -> Golden_Gun(p, true);
                case "烈焰黄金枪" -> Golden_Gun(p, false);
                case "风起云涌" -> Gathering_Storm(p);
                case "新星炸弹-灾变" -> Nova_Bomb(p, false);
                case "新星炸弹-涡流" -> Nova_Bomb(p, true);
                case "鬼灵利刃" -> Spectral_Blades(p);
                case "哨兵圣盾"->Sentinel_Shield(p);
                case "黎明"->Daybreak(p);
                //case "test" -> test(p);
                //case "playSound"->playSound(p);
            }
        }
    }
    //测试代码
    public void test(Player p){
        //手持物品
        ItemStack sword=new ItemStack(Material.GOLDEN_SWORD);
        sword.addUnsafeEnchantment(Enchantment.BINDING_CURSE,5);
        ItemMeta swordMeta= sword.getItemMeta();
        swordMeta.setDisplayName("Well_Of_Radiance");
        sword.setItemMeta(swordMeta);
        //召唤盔甲架,设置盔甲架,添加tag,修改名字,旋转
        Entity e=p.getWorld().spawnEntity(p.getLocation(),EntityType.ARMOR_STAND);

        if(e.getLocation().getBlock().getType()!=Material.AIR){
            e.setGravity(false);
        }
        e.addScoreboardTag("Pose");
        ArmorStand as= (ArmorStand) e;
        as.setRotation(DestinyMain.Te.getv1(),DestinyMain.Te.getv2());
        Bukkit.broadcastMessage(DestinyMain.Te.getv1()+"/"+DestinyMain.Te.getv2());
        //手臂旋转
        as.setLeftArmPose(new EulerAngle(0,0,0));
        as.setRightArmPose(new EulerAngle(DestinyMain.Te.getX(),DestinyMain.Te.getY(),DestinyMain.Te.getZ()));
        Bukkit.broadcastMessage(DestinyMain.Te.getX()+"/"+DestinyMain.Te.getY()+"/"+DestinyMain.Te.getZ());
        as.setArms(true);

    }


    public void playSound(Player p){
        p.getWorld().playSound(p.getLocation(),Sound.AMBIENT_BASALT_DELTAS_MOOD,SoundCategory.PLAYERS,10,0.8f);
    }


    //雷霆冲击
    public void Thunder_Crash(Player p){
        //位置储存,经验储存
        Position position=new Position();
        Exp exp=new Exp(p.getLevel(),p.getExp());
        p.setVelocity(p.getVelocity().setY(p.getVelocity().getY()+0.7));
        p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));

        //鞘翅,死绑,并强制替换护甲
        //===============================================
        ItemStack i=new ItemStack(Material.ELYTRA);
        ItemMeta im=i.getItemMeta();
        im.setDisplayName("§f雷霆冲击");
        i.setItemMeta(im);
        i.addUnsafeEnchantment(Enchantment.BINDING_CURSE,5);
        p.getEquipment().setChestplate(i);
        p.addScoreboardTag("Thunder_Crash");
        new BukkitRunnable() {
            int time=110;
            boolean op=true;
            Location location=p.getLocation();
            double MaxHeight=10;
            @Override
            public void run() {
                if(p.isGliding()){
                    double Height=p.getLocation().getY()-location.getY();
                    double Y=p.getLocation().getDirection().getY();
                    double finalY=0;
                    if(Y<0){
                        finalY=Y;
                    }else if(p.getLocation().getPitch()<-35){
                        Location lo=p.getLocation();
                        lo.setPitch(-35);
                        p.teleport(lo);
                    }
                    else{
                        finalY=Y*(Math.pow(1-(Height/MaxHeight),2));
                    }

                    p.setVelocity(p.getLocation().getDirection().multiply(0.65f).setY(finalY));
                }
                //进入滑行,赋予初动量,开始撞击计划
                if(op) {
                    p.setGliding(true);
                    p.setVelocity(p.getPlayer().getLocation().getDirection().multiply(0.85));
                    p.addScoreboardTag("No_Fall_Damage");
                    //给予5秒抗性2,播放爆炸和凋零复活,备份经验值,开始
                    p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,110,2,false));
                    p.getWorld().playSound(p.getLocation(),Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS,200,0.5f);
                    p.getWorld().playSound(p.getLocation(),Sound.ENTITY_WITHER_SPAWN, SoundCategory.PLAYERS,200,0.8f);
                    op=false;
                }
                if(time<0||p.isDead()||p.isFlying()){
                    p.getScoreboardTags().remove("Thunder_Crash");
                    p.getScoreboardTags().remove("No_Fall_Damage");
                    if(p.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)){
                        p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                    }
                    p.getEquipment().setChestplate(new ItemStack(Material.AIR));
                    this.cancel();
                }
                time--;
            }
        }.runTaskTimer(DestinyMain.getPlugin(DestinyMain.class),5,1);
    }

    //光焰之井
    public void Well_Of_Radiance(Player p){

        //手持物品
        ItemStack sword=new ItemStack(Material.GOLDEN_SWORD);
        sword.addUnsafeEnchantment(Enchantment.BINDING_CURSE,5);
        ItemMeta swordMeta= sword.getItemMeta();
        swordMeta.setDisplayName("Well_Of_Radiance");
        sword.setItemMeta(swordMeta);
        //召唤盔甲架,设置盔甲架,添加tag,修改名字,旋转
        Entity e=p.getWorld().spawnEntity(p.getLocation().subtract(0,0.5f,0),EntityType.ARMOR_STAND);

        if(e.getLocation().getBlock().getType()!=Material.AIR){
            e.setGravity(false);
        }
        ArmorStand as= (ArmorStand) e;
        //无敌
        as.setInvulnerable(true);
        //可视
        as.setVisible(false);
        //碰撞体积
        as.setCollidable(false);
        //底座
        as.setBasePlate(false);
        //自定名字可视
        e.setCustomNameVisible(false);
        //添加Tag
        e.addScoreboardTag("Well_Of_Radiance_ArmorStand");
        e.addScoreboardTag(p.getUniqueId().toString());
        //改名
        e.setCustomName(ChatColor.GOLD+"Well_Of_Radiance_ArmorStand");
        //手臂旋转
        as.setRightArmPose(new EulerAngle(1.40f,1.55f,0));

        //持剑
        as.getEquipment().setItemInMainHand(sword);

        //插剑并且下降
        p.setVelocity(new Vector().setY(-0.1f));

        //盔甲架下降后关闭重力
        BukkitRunnable ArmorSet=new BukkitRunnable() {
            int time=40;
            @Override
            public void run() {
                if(as.isOnGround()){
                    as.setGravity(false);
                    e.getLocation().setY(e.getLocation().getY()-0.3f);
                    Well_Of_Radiance_Listener.Recovery(as);
                    for(Entity entity:as.getWorld().getNearbyEntities(as.getLocation(),5,5,5)){
                        if(entity instanceof LivingEntity le&&le.getUniqueId()!=p.getUniqueId() &&le!=as){
                            le.setFireTicks(100);
                            le.damage(5.0,p);
                            le.setNoDamageTicks(0);
                        }
                    }
                    this.cancel();
                    return;
                }
                if(time<0||!as.hasGravity()){
                    this.cancel();
                    return;
                }
                time--;
            }
        };
        if(!p.isOnGround()){
            ArmorSet.runTaskTimer(DestinyMain.getPlugin(DestinyMain.class),0,5);
        }else{
            Well_Of_Radiance_Listener.Recovery(as);
            for(Entity entity:as.getWorld().getNearbyEntities(as.getLocation(),5,5,5)){
                if(entity instanceof LivingEntity le&&le.getUniqueId()!=p.getUniqueId()&&!(le instanceof ArmorStand)){
                    le.setFireTicks(100);
                    le.damage(5.0,p);
                    le.setNoDamageTicks(0);
                }
            }
        }
        p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
    }

    public void Shadow_Shot(Player p,boolean isMobius){
        //new弓和弓箭,弓箭数量,改名
        ItemStack Bow=new ItemStack(Material.BOW);
        ItemStack Arrow=new ItemStack(Material.ARROW);
        ItemMeta BowMeta=Bow.getItemMeta();
        ItemMeta ArrowMeta=Arrow.getItemMeta();
        int Amount;
        p.addScoreboardTag("Shadow_Shot_Using");
        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,110,2,false));
        //重命名
        //Deadfall/Mobius Quiver
        if(isMobius){
            BowMeta.setDisplayName("莫比乌斯箭袋");
            ArrowMeta.setDisplayName("莫比乌斯弓箭");
            Bow.setItemMeta(BowMeta);
            Arrow.setItemMeta(ArrowMeta);
            Arrow.setAmount(2);
            Amount=2;
            //添加超能使用中Tag
            p.addScoreboardTag("Shadow_Shot_Mobius");
            p.addScoreboardTag("Shadow_Shot_Arrow_1");
        }else{
            BowMeta.setDisplayName("狩猎陷阱");
            ArrowMeta.setDisplayName("狩猎陷阱弓箭");
            Bow.setItemMeta(BowMeta);
            Arrow.setItemMeta(ArrowMeta);
            Amount=1;
            //添加超能使用中Tag
            p.addScoreboardTag("Shadow_Shot_DeadFall");
        }
        //设置弓耐久
        Damageable bd=(Damageable) Bow.getItemMeta();
        bd.setDamage(Bow.getType().getMaxDurability()-Amount);
        Bow.setItemMeta((ItemMeta) bd);


        //给弓和弓箭附魔,绑5
        Bow.addUnsafeEnchantment(Enchantment.BINDING_CURSE,5);
        Arrow.addUnsafeEnchantment(Enchantment.BINDING_CURSE,5);
        //放置弓于主手,弓箭于副手
        p.getEquipment().setItemInOffHand(Arrow);
        p.getEquipment().setItemInMainHand(Bow);
        //添加超能使用中Tag
    }

    //风起云涌
    public void Gathering_Storm(Player p){
        p.addScoreboardTag("Gathering_Storm");
        ItemStack Trident=new ItemStack(Material.TRIDENT);
        Trident.addUnsafeEnchantment(Enchantment.BINDING_CURSE,5);
        ItemMeta TridentMeta=Trident.getItemMeta();
        TridentMeta.setDisplayName("叉子");
        Trident.setItemMeta(TridentMeta);
        p.getInventory().setItemInMainHand(Trident);
        p.addScoreboardTag("Gathering_Storm_Using");
        //叉子定时
        BukkitRunnable Timer=new BukkitRunnable() {
            int time=DestinyMain.H.getSuper_Power_Activate_Time();
            @Override
            public void run() {
                if(time<=0){
                    p.getInventory().removeItem(Trident);
                    p.getScoreboardTags().remove("Gathering_Storm_Using");
                    p.getScoreboardTags().remove("Gathering_Storm");
                    this.cancel();
                    return;
                }
                if(!p.getScoreboardTags().contains("Gathering_Storm_Using")){
                    p.getScoreboardTags().remove("Gathering_Storm");
                    this.cancel();
                }
                time--;
            }
        };
        Timer.runTaskTimer(DestinyMain.getPlugin(DestinyMain.class),0,20);
    }

    //新星炸弹
    public void Nova_Bomb(Player p,boolean isVortex){
        ItemStack sb=new ItemStack(Material.SNOWBALL);
        sb.addUnsafeEnchantment(Enchantment.BINDING_CURSE,5);
        ItemMeta sbMate=sb.getItemMeta();
        if(isVortex){
            sbMate.setDisplayName("新星炸弹-涡流");
            p.addScoreboardTag("Nova_Bomb_Using");
            p.addScoreboardTag("Nova_Bomb_Vortex");
//            p.addScoreboardTag("Nova_Damage_Reduce");
        }else{
            sbMate.setDisplayName("新星炸弹-灾变");
            p.addScoreboardTag("Nova_Bomb_Using");
            p.addScoreboardTag("Nova_Bomb_Cataclysm");
//            p.addScoreboardTag("Nova_Damage_Reduce");
        }
        sb.setItemMeta(sbMate);
        p.getInventory().setItemInMainHand(sb);
    }

    //黄金枪
    public void Golden_Gun(Player p,boolean is3){
        final int total_time;
        //new枪,附魔,使用时间
        ItemStack Gun=new ItemStack(Material.GOLDEN_HOE);
        Gun.addUnsafeEnchantment(Enchantment.BINDING_CURSE,5);
        //添加超能使用中Tag
        p.addScoreboardTag("Golden_Gun_Using");
        ItemMeta GunMeta=Gun.getItemMeta();
        if(is3){
            total_time=25;
            p.addScoreboardTag("Golden_Gun_3");
            GunMeta.setDisplayName("精准黄金枪");
            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,500,2,false));
        }else{
            total_time=15;
            p.addScoreboardTag("Golden_Gun_6");
            GunMeta.setDisplayName("烈焰黄金枪");
            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,240,1,false));
        }
        Gun.setItemMeta(GunMeta);
        p.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
        p.getInventory().setItemInMainHand(Gun);
    }

    //鬼灵利刃
    public void Spectral_Blades(Player p){
        ItemStack sword=new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta swordMeta=sword.getItemMeta();
        swordMeta.setDisplayName("鬼灵利刃");
        sword.setItemMeta(swordMeta);
        sword.addUnsafeEnchantment(Enchantment.BINDING_CURSE,5);
        p.addScoreboardTag("Spectral_Blades_Using");
        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,500,2,false));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,500,0,false));
        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,100,4,false));
        p.getInventory().setItemInMainHand(sword);
        p.getWorld().playSound(p.getLocation(),Sound.ITEM_ARMOR_EQUIP_LEATHER,5.0f,0.5f);
        p.getWorld().playSound(p.getLocation(),Sound.ITEM_ARMOR_EQUIP_TURTLE,5.0f,0.5f);
        BukkitRunnable SB=new BukkitRunnable() {
            int time=25;
            @Override
            public void run() {
                if(time<=0||p.isDead()||p.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)==null){
                    p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                    p.removePotionEffect(PotionEffectType.INVISIBILITY);
                    p.removePotionEffect(PotionEffectType.SPEED);
                    Destiny2.Misc.Misc.Remove_Items(p);
                    p.getScoreboardTags().remove("Spectral_Blades_Using");
                    this.cancel();
                    return;
                }
                p.getWorld().spawnParticle(Particle.DRIPPING_OBSIDIAN_TEAR,p.getLocation().add(0,1,0),50,0.75f,0.75f,0.75f);
                time--;
            }
        };
        SB.runTaskTimer(DestinyMain.getPlugin(DestinyMain.class),0,20);
    }

    //哨兵圣盾
    public void Sentinel_Shield(Player p){
        ItemStack Shield=new ItemStack(Material.SHIELD);
        ItemStack Sword=new ItemStack(Material.DIAMOND_SWORD);
        Shield.addUnsafeEnchantment(Enchantment.BINDING_CURSE,5);
        Shield.addUnsafeEnchantment(Enchantment.DURABILITY,5);
        Sword.addUnsafeEnchantment(Enchantment.BINDING_CURSE,5);
        ItemMeta ShieldMate=Shield.getItemMeta();
        ItemMeta SwordMate=Sword.getItemMeta();
        ShieldMate.setDisplayName("圣盾");
        SwordMate.setDisplayName("圣剑");
        Shield.setItemMeta(ShieldMate);
        Sword.setItemMeta(SwordMate);
        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,500,2,false));
        p.addScoreboardTag("Sentinel_Shield_Using");
        p.getInventory().setItemInOffHand(Shield);
        p.getInventory().setItemInMainHand(Sword);
        BukkitRunnable Shielder=new BukkitRunnable() {
            int time=25;
            @Override
            public void run() {
                if(time<=0||p.isDead()||!p.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)){
                    p.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                    p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                    p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                    p.getScoreboardTags().remove("Sentinel_Shield_Using");
                    this.cancel();
                    return;
                }
                time--;
            }
        };
        Shielder.runTaskTimer(DestinyMain.getPlugin(DestinyMain.class),0,20);
    }

    //黎明
    public void Daybreak(Player p){
        ItemStack Sword=new ItemStack(Material.GOLDEN_SWORD);
        ItemMeta SwordMata=Sword.getItemMeta();
        SwordMata.setDisplayName("黎明");
        List<String> lore=new LinkedList<>();
        lore.add("§e我将会是黎明中的凤凰！");
        lore.add("§e世界将会被我照亮！");
        lore.add("§e黎明之剑将守护世界！");
        SwordMata.setLore(lore);
        Sword.setItemMeta(SwordMata);
        Sword.addUnsafeEnchantment(Enchantment.BINDING_CURSE,5);
        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,500,2,false));
        p.getInventory().setItemInMainHand(Sword);
        p.setVelocity(p.getVelocity().multiply(0.1f).setY(0.5));
        p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,10,0,false));
        p.addScoreboardTag("Daybreak_Using");
        BukkitRunnable Daybreak=new BukkitRunnable() {
            int time=25;
            @Override
            public void run() {
                if(p.getInventory().getItemInMainHand().getType()!=Material.AIR&&time<=0||p.isDead()
                        ||((Damageable) p.getInventory().getItemInMainHand().getItemMeta()).getDamage()>=(Material.GOLDEN_SWORD).getMaxDurability()
                        ||p.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)==null){
                    p.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                    p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                    p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                    if(p.hasPotionEffect(PotionEffectType.SLOW_FALLING)){
                        p.removePotionEffect(PotionEffectType.SLOW_FALLING);
                    }
                    p.getScoreboardTags().remove("Daybreak_Using");
                    this.cancel();
                    return;
                }
                Test_Listener.active(p.getLocation());
                time--;
            }
        };
        Daybreak.runTaskTimer(DestinyMain.getPlugin(DestinyMain.class),0,20);
    }
}