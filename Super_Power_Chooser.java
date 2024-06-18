package Destiny2;

import Destiny2.Misc.Misc;
import Destiny2.Misc.Summon;
import Destiny2.Misc.Test_Listener;
import Destiny2.Super_Power.*;
import Destiny2.Variable_Construction.Equipments;
import Destiny2.Variable_Construction.Exp;
import Destiny2.Variable_Construction.Items;
import net.minecraft.server.v1_16_R3.EntityBlaze;
import net.minecraft.server.v1_16_R3.EntityVillager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.libs.org.eclipse.sisu.Nullable;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftBlaze;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftVillager;
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
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.bukkit.inventory.meta.Damageable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Super_Power_Chooser implements Listener{
    public static Map<Player, Equipments> Player_Equipment=DestinyMain.Player_Equipment;
    static LinkedList<Player> Super_Power_User=DestinyMain.Super_Power_User;
    public static Map<Player, BukkitTask> task=new HashMap<>();
    public Map<Player, Chaos_Reach.LaserRunnable> lasers=Chaos_Reach.lasers;
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
                case "�������" -> Thunder_Crash(p);
                case "����֮��" -> Well_Of_Radiance(p);
                case "��������" -> Shadow_Shot(p, false);
                case "Ī����˹����" -> Shadow_Shot(p, true);
                case "��׼�ƽ�ǹ" -> Golden_Gun(p, true);
                case "����ƽ�ǹ" -> Golden_Gun(p, false);
                case "������ӿ" -> Gathering_Storm(p);
                case "����ը��-�ֱ�" -> Nova_Bomb(p, false);
                case "����ը��-����" -> Nova_Bomb(p, true);
                case "��������" -> Spectral_Blades(p);
                case "�ڱ�ʥ��"->Sentinel_Shield(p);
                case "����"->Daybreak(p);
                case "���е�Ļ"->Blade_Barrage(p);
                case "��Ĭ��Х"->Silence_and_Squall(p);
                //case "test" -> test(p);
                //case "playSound"->playSound(p);
                case "����֮��"-> {
                    try {
                        lasers.put(p, new Chaos_Reach.LaserRunnable(p));
                        lasers.get(p).runTaskTimer(DestinyMain.getPlugin(DestinyMain.class), 5, 1);
                        Chaos_Reach.Chaos_Reach_Start(p);
                    } catch (ReflectiveOperationException e) {
                        e.printStackTrace();
                    }
                }
                case "ȼ�նʹ�"->Burning_Maul(p);
                case "�������"->Storm_Trance(p);
                case "����֮��"->Hammer_of_Sol(p);
                case "moveto"->navigateTo(p,3+p.getLocation().getX()+Math.random()*4*Misc.RandomPositiveMinus(),p.getLocation().getY(),3+p.getLocation().getZ()+Math.random()*4*Misc.RandomPositiveMinus());
                case "movetoo"->navigateToo(p,3+p.getLocation().getX()+Math.random()*4*Misc.RandomPositiveMinus(),p.getLocation().getY(),3+p.getLocation().getZ()+Math.random()*4*Misc.RandomPositiveMinus());

            }
        }
    }
    //���Դ���
    public void test(Player p){
        //�ֳ���Ʒ
        ItemStack sword=new ItemStack(Material.GOLDEN_SWORD);
        sword.addUnsafeEnchantment(Enchantment.BINDING_CURSE,5);
        ItemMeta swordMeta= sword.getItemMeta();
        swordMeta.setDisplayName("Well_Of_Radiance");
        sword.setItemMeta(swordMeta);
        //�ٻ����׼�,���ÿ��׼�,���tag,�޸�����,��ת
        Entity e=p.getWorld().spawnEntity(p.getLocation(),EntityType.ARMOR_STAND);

        if(e.getLocation().getBlock().getType()!=Material.AIR){
            e.setGravity(false);
        }
        e.addScoreboardTag("Pose");
        ArmorStand as= (ArmorStand) e;
        as.setRotation(DestinyMain.Te.getv1(),DestinyMain.Te.getv2());
        Bukkit.broadcastMessage(DestinyMain.Te.getv1()+"/"+DestinyMain.Te.getv2());
        //�ֱ���ת
        as.setLeftArmPose(new EulerAngle(0,0,0));
        as.setRightArmPose(new EulerAngle(DestinyMain.Te.getX(),DestinyMain.Te.getY(),DestinyMain.Te.getZ()));
        Bukkit.broadcastMessage(DestinyMain.Te.getX()+"/"+DestinyMain.Te.getY()+"/"+DestinyMain.Te.getZ());
        as.setArms(true);

    }

    //�����ƶ�����
    public boolean navigateTo(Player p, double x, double y, double z) {
        Villager villager=null;
        for(Entity e:p.getNearbyEntities(20,20,20)){
            if(e instanceof Villager v){
                villager=v;
                break;
            }
        }
        if(villager==null){
            return false;
        }
        EntityVillager v = ((CraftVillager) villager).getHandle();
        return v.getNavigation().a(x, y, z, 0.84);
    }

    public boolean navigateToo(Player p, double x, double y, double z) {
        Blaze Blaze=null;
        for(Entity e:p.getNearbyEntities(20,20,20)){
            if(e instanceof Blaze v){
                Blaze=v;
                break;
            }
        }
        if(Blaze==null){
            return false;
        }
        EntityBlaze b=((CraftBlaze) Blaze).getHandle();
        b.setGoalTarget(null);
        return b.getNavigation().a(x, y, z, 1f);
    }



    public void playSound(Player p){
        p.getWorld().playSound(p.getLocation(),Sound.AMBIENT_BASALT_DELTAS_MOOD,SoundCategory.PLAYERS,10,0.8f);
    }

    //Ԥ��ʹ�ö������
    public static boolean add_User_List(Player p){
        if(!Super_Power_User.contains(p)){
            Super_Power_User.add(p);
            return false;
        }
        return true;
    }
    public static void remove_User_List(Player p){
        Super_Power_User.remove(p);
    }

    //�������
    public void Thunder_Crash(Player p){
//        //λ�ô���,���鴢��
//        Position position=new Position();
//        Exp exp=new Exp(p.getLevel(),p.getExp());
//        p.setVelocity(p.getVelocity().setY(p.getVelocity().getY()+0.7));
//        p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));

        //�ʳ�,����,��ǿ���滻����
        //===============================================

        if(add_User_List(p)){
            return;
        }
        Thunder_Crash_Listener.Thunder_Crash_User.add(p);//������
        Player_Equipment.put(p,new Equipments(p));//�������װ��
        ItemStack i=new ItemStack(Material.ELYTRA);
        ItemMeta im=i.getItemMeta();
        im.setDisplayName("��f�������");
        i.setItemMeta(im);
        i.addUnsafeEnchantment(Enchantment.BINDING_CURSE,5);
        p.getEquipment().setChestplate(i);
        task.put(p,new BukkitRunnable() {
            int time=110;
            boolean initialized=false;
            final Location location=p.getLocation();
            final double MaxHeight=10;
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
                //���뻬��,���������,��ʼײ���ƻ�
                if(!initialized) {
                    p.setGliding(true);
                    p.setVelocity(p.getLocation().getDirection().multiply(0.85));
                    //����5�뿹��2,���ű�ը�͵��㸴��
                    p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,110,2,false));
                    p.getWorld().playSound(p.getLocation(),Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS,200,0.5f);
                    p.getWorld().playSound(p.getLocation(),Sound.ENTITY_WITHER_SPAWN, SoundCategory.PLAYERS,200,0.8f);
                    initialized=true;
                }
                if(Misc.NearlyTarget(p,0.5,0.5,0.5)!=null){
                    p.getInventory().setChestplate(new ItemStack(Material.AIR));
                }
                if(time<=0||p.isDead()||p.isFlying()){
                    remove_User_List(p);
                    Thunder_Crash_Listener.Thunder_Crash_User.remove(p);
                    if(p.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)){
                        p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                    }
                    p.getInventory().setChestplate(Player_Equipment.get(p).getChest());
                    Player_Equipment.remove(p);
                    this.cancel();
                }
                time--;
            }
        }.runTaskTimer(DestinyMain.getPlugin(DestinyMain.class),5,1));
    }

    //����֮��
    public void Well_Of_Radiance(Player p){

        //�ֳ���Ʒ
        ItemStack sword=new ItemStack(Material.GOLDEN_SWORD);
        sword.addUnsafeEnchantment(Enchantment.BINDING_CURSE,5);
        ItemMeta swordMeta= sword.getItemMeta();
        swordMeta.setDisplayName("Well_Of_Radiance");
        sword.setItemMeta(swordMeta);

        ArmorStand as=(ArmorStand) p.getWorld().spawnEntity(p.getLocation(),EntityType.ARMOR_STAND);
        as.addScoreboardTag("Clean");
        //����б�
        Well_Of_Radiance_Listener.ArmorStands.add(as);
        //�޵�
        as.setInvulnerable(false);
        //����
        as.setVisible(false);
        //��ײ���
        as.setCollidable(false);
        //����
        as.setBasePlate(false);
        //�Զ����ֿ���
        as.setCustomNameVisible(false);
        //���Tag
        Well_Of_Radiance_Listener.ArmorStands.add(as);
        Well_Of_Radiance_Listener.Well_Of_Radiance_Owner.put(as,p);
        as.addScoreboardTag("Well_Of_Radiance_ArmorStand");
        as.addScoreboardTag(p.getUniqueId().toString());
        //����
        as.setCustomName(ChatColor.GOLD+"Well_Of_Radiance_ArmorStand");
        //�ֱ���ת
        as.setRightArmPose(new EulerAngle(1.40f,1.55f,0));

        //�ֽ�
        as.getEquipment().setItemInMainHand(sword);

        //�彣�����½�
        p.setVelocity(new Vector().setY(-0.1f));

        //���׼��½���ر�����
        BukkitRunnable ArmorSet=new BukkitRunnable() {
            int time=100;
            @Override
            public void run() {
                if(as.isOnGround()){
                    as.setGravity(false);
                    Well_Of_Radiance_Listener.Recovery(as);
                    for(Entity entity:as.getWorld().getNearbyEntities(as.getLocation(),Well_Of_Radiance_Listener.Well_Of_Radiance_Radius,Well_Of_Radiance_Listener.Well_Of_Radiance_Radius,Well_Of_Radiance_Listener.Well_Of_Radiance_Radius)){
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
            ArmorSet.runTaskTimer(DestinyMain.getPlugin(DestinyMain.class),0,1);
        }else{
            Well_Of_Radiance_Listener.Recovery(as);
            as.setGravity(false);
            for(Entity entity:as.getWorld().getNearbyEntities(as.getLocation(),Well_Of_Radiance_Listener.Well_Of_Radiance_Radius,Well_Of_Radiance_Listener.Well_Of_Radiance_Radius,Well_Of_Radiance_Listener.Well_Of_Radiance_Radius)){
                if(entity instanceof LivingEntity le&&le.getUniqueId()!=Well_Of_Radiance_Listener.Well_Of_Radiance_Owner.get(as).getUniqueId() &&!(le instanceof ArmorStand)){
                    le.setFireTicks(100);
                    le.damage(5.0,p);
                    le.setNoDamageTicks(0);
                }
            }
        }
        p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
    }

    //��ռ�
    public void Shadow_Shot(Player p,boolean isMobius){
        if(add_User_List(p)){
            return;
        }
        //new���͹���,��������,����
        ItemStack Bow=new ItemStack(Material.BOW);
        ItemStack Arrow=new ItemStack(Material.ARROW);
        ItemMeta BowMeta=Bow.getItemMeta();
        ItemMeta ArrowMeta=Arrow.getItemMeta();
        int Amount;
        //���User
        Shadow_Shot_Fix_Listener.Shadow_Shot_User.add(p);
//        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,110,2,false));
        //������
        //Deadfall/Mobius Quiver
        if(isMobius){
            BowMeta.setDisplayName("Ī����˹����");
            ArrowMeta.setDisplayName("Ī����˹����");
            Bow.setItemMeta(BowMeta);
            Arrow.setItemMeta(ArrowMeta);
            Arrow.setAmount(2);
            Amount=2;
            //��ӳ���ʹ����Tag
            Shadow_Shot_Fix_Listener.Amount.put(p,Amount);
            Shadow_Shot_Fix_Listener.Using_Mobius.add(p);
        }else{
            BowMeta.setDisplayName("��������");
            ArrowMeta.setDisplayName("�������幭��");
            Bow.setItemMeta(BowMeta);
            Arrow.setItemMeta(ArrowMeta);
            Amount=1;
        }
        //���ù��;�
        Damageable bd=(Damageable) Bow.getItemMeta();
        bd.setDamage(Bow.getType().getMaxDurability()-Amount);
        Bow.setItemMeta((ItemMeta) bd);


        //�����͹�����ħ,��5
        Bow.addUnsafeEnchantment(Enchantment.BINDING_CURSE,5);
        Arrow.addUnsafeEnchantment(Enchantment.BINDING_CURSE,5);
        //���ù�������,�����ڸ���
        p.getEquipment().setItemInOffHand(Arrow);
        p.getEquipment().setItemInMainHand(Bow);
    }

    //������ӿ
    public void Gathering_Storm(Player p){
        if(add_User_List(p)){
            return;
        }
        Gathering_Storm_Listener.Gathering_Storm_User.add(p);

        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.PLAYERS, 2.0f, 0.6f);

        ItemStack Trident=new ItemStack(Material.TRIDENT);
        Trident.addUnsafeEnchantment(Enchantment.BINDING_CURSE,5);
        ItemMeta TridentMeta=Trident.getItemMeta();
        TridentMeta.setDisplayName("����");
        Trident.setItemMeta(TridentMeta);
        p.getInventory().setItemInMainHand(Trident);
        //���Ӷ�ʱ
        BukkitRunnable Timer=new BukkitRunnable() {
            int time=Gathering_Storm_Listener.Super_Power_Activate_Time;
            @Override
            public void run() {
                if(time<=0){
                    p.getInventory().removeItem(Trident);
                    remove_User_List(p);
                    Gathering_Storm_Listener.Gathering_Storm_User.remove(p);
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

    //����ը��
    public void Nova_Bomb(Player p,boolean isVortex){
        if(add_User_List(p)){
            return;
        }

        ItemStack sb=new ItemStack(Material.SNOWBALL);
        sb.addUnsafeEnchantment(Enchantment.BINDING_CURSE,5);
        ItemMeta sbMate=sb.getItemMeta();
        if(isVortex){
            sbMate.setDisplayName("����ը��-����");
//            p.addScoreboardTag("Nova_Bomb_Using");
//            p.addScoreboardTag("Nova_Bomb_Vortex");
//            p.addScoreboardTag("Nova_Damage_Reduce");
            Nova_Bomb_Listener.Cataclysm_Bomb_Model.put(p,false);
        }else{
            sbMate.setDisplayName("����ը��-�ֱ�");
//            p.addScoreboardTag("Nova_Bomb_Using");
//            p.addScoreboardTag("Nova_Bomb_Cataclysm");
//            p.addScoreboardTag("Nova_Damage_Reduce");
            Nova_Bomb_Listener.Cataclysm_Bomb_Model.put(p,true);
        }
        Nova_Bomb_Listener.Nova_Bomb_User.add(p);
        sb.setItemMeta(sbMate);
        p.getInventory().setItemInMainHand(sb);
    }

    //�ƽ�ǹ
    public void Golden_Gun(Player p,boolean is3){
        if(add_User_List(p)){
            return;
        }
        Golden_Gun_Listener.Golden_Gun_User.add(p);
        Player_Equipment.put(p,new Equipments(p));
        ItemStack Gun;
        if(is3){
            Gun=Items.getGolden_Gun(true);
            Golden_Gun_Listener.Ammo3.add(p);
            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,500,2,false));
        }else{
            Gun=Items.getGolden_Gun(false);
            Golden_Gun_Listener.Ammo6.add(p);
            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,240,1,false));
        }
        p.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
        p.getInventory().setItemInMainHand(Gun);
    }
//    public void Golden_Gun(Player p,boolean is3){
//        final int total_time;
//        //newǹ,��ħ,ʹ��ʱ��
//        ItemStack Gun=new ItemStack(Material.GOLDEN_HOE);
//        Gun.addUnsafeEnchantment(Enchantment.BINDING_CURSE,5);
//        //��ӳ���ʹ����Tag
//        p.addScoreboardTag("Golden_Gun_Using");
//        ItemMeta GunMeta=Gun.getItemMeta();
//        if(is3){
//            total_time=25;
//            p.addScoreboardTag("Golden_Gun_3");
//            GunMeta.setDisplayName("��׼�ƽ�ǹ");
//            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,500,2,false));
//        }else{
//            total_time=15;
//            p.addScoreboardTag("Golden_Gun_6");
//            GunMeta.setDisplayName("����ƽ�ǹ");
//            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,240,1,false));
//        }
//        Gun.setItemMeta(GunMeta);
//        p.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
//        p.getInventory().setItemInMainHand(Gun);
//    }

    //��������
    public void Spectral_Blades(Player p){
        if(add_User_List(p)){
            return;
        }
        Spectral_Blades_Listener.Spectral_Blades_User.add(p);
        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,500,2,false));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,500,0,false));
        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,100,4,false));
        p.getInventory().setItemInMainHand(Items.getSpectral_Blades_Sword());
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
                    remove_User_List(p);
                    Spectral_Blades_Listener.Spectral_Blades_User.remove(p);
                    this.cancel();
                    return;
                }
                p.getWorld().spawnParticle(Particle.DRIPPING_OBSIDIAN_TEAR,p.getLocation().add(0,1,0),50,0.75f,0.75f,0.75f);
                time--;
            }
        };
        SB.runTaskTimer(DestinyMain.getPlugin(DestinyMain.class),0,20);
    }

    //�ڱ�ʥ��(Ч��������)
    public void Sentinel_Shield(Player p){
        if(add_User_List(p)){
            return;
        }
        Player_Equipment.put(p,new Equipments(p));
        Sentinel_Shield_Listener.Sentinel_User.add(p);
        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,500,2,false));
        p.getInventory().setItemInMainHand(Items.getSentinel_Sword());
        p.getInventory().setItemInOffHand(Items.getSentinel_Shield());
        BukkitRunnable Shielder=new BukkitRunnable() {
            int time=25;
            @Override
            public void run() {
                if(time<=0||p.isDead()||!p.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)){
                    p.getInventory().setItemInOffHand(Player_Equipment.get(p).getOffHand());
                    p.getInventory().setItemInMainHand(Player_Equipment.get(p).getMainHand());
                    p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                    Sentinel_Shield_Listener.Sentinel_User.remove(p);
                    remove_User_List(p);
                    this.cancel();
                    return;
                }
                time--;
            }
        };
        Shielder.runTaskTimer(DestinyMain.getPlugin(DestinyMain.class),0,20);
    }

    //����
    public void Daybreak(Player p){
        if(add_User_List(p)){
            return;
        }
        Daybreak_Listener.Day_Break_User.add(p);

        DestinyMain.Player_Exp.put(p,new Exp(p));
        Exp.CoolTime(15,p);

        Player_Equipment.put(p,new Equipments(p));

        p.getInventory().setItemInMainHand(Items.getDaybreak_Sword());
        p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,10,0,false));
        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,500,2,false));
        p.setVelocity(p.getVelocity().multiply(0.1f).setY(0.5));



        BukkitRunnable Daybreak=new BukkitRunnable() {
            int time=25;
            @Override
            public void run() {
                if(time<=0||p.isDead()
                        ||p.getInventory().getItemInMainHand().getType()==Material.AIR
                        ||((Damageable) p.getInventory().getItemInMainHand().getItemMeta()).getDamage()>=(Material.GOLDEN_SWORD).getMaxDurability()
                        ||p.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)==null){
                    p.getInventory().setItemInOffHand(Player_Equipment.get(p).getOffHand());
                    p.getInventory().setItemInMainHand(Player_Equipment.get(p).getMainHand());
                    p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                    if(p.hasPotionEffect(PotionEffectType.SLOW_FALLING)){
                        p.removePotionEffect(PotionEffectType.SLOW_FALLING);
                    }
                    Player_Equipment.remove(p);
                    Daybreak_Listener.Day_Break_User.remove(p);
                    remove_User_List(p);
                    this.cancel();
                    return;
                }
                Test_Listener.active(p.getLocation());
                time--;
            }
        };
        Daybreak.runTaskTimer(DestinyMain.getPlugin(DestinyMain.class),0,20);
    }

    //���е�Ļ
    public void Blade_Barrage(Player p){
        if(add_User_List(p)){
            return;
        }

        p.getWorld().playSound(p.getLocation(),Sound.ENTITY_EXPERIENCE_ORB_PICKUP,4.0f,0.5f);
        p.getWorld().playSound(p.getLocation(),Sound.ITEM_FLINTANDSTEEL_USE,4.0f,0.5f);
        p.getWorld().playSound(p.getLocation(),Sound.ENTITY_SKELETON_AMBIENT,4.0f,0.25f);
        p.getWorld().playSound(p.getLocation(),Sound.ENTITY_SKELETON_HORSE_HURT,4.0f,0.25f);
        p.setVelocity(p.getVelocity().multiply(0.1).setY(0.75f));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING,30,4,false));

        BukkitRunnable shoot=new BukkitRunnable() {
            boolean First=true;

            @Override
            public void run() {
                if(First){
                    Arrow ar=Spawn_Arrow(p,true);
                    for(int arrows=1;arrows<Blade_Barrage_Listener.Per_Arrows;arrows++){
                        if(p.isDead()){
                            First=false;
                            break;
                        }
                        Arrow_Rotate(ar, p);
                    }
                }else{
                    Arrow ar=Spawn_Arrow(p,false);
                    for(int arrows=1;arrows<Blade_Barrage_Listener.Per_Arrows;arrows++){
                        if(p.isDead()){
                            break;
                        }
                        Arrow_Rotate(ar, p);
                    }
                }

                if(!First||p.isDead()){
                    this.cancel();
                    remove_User_List(p);
                }
                First=false;
            }
        };
        shoot.runTaskTimer(DestinyMain.getPlugin(DestinyMain.class),15,5);



    }
    private void Arrow_Rotate(Arrow ar, Player p) {
        Vector vector;
        Arrow arrow;
        vector=ar.getVelocity().clone();
        arrow=(Arrow)p.getWorld().spawnEntity(ar.getLocation(), EntityType.ARROW);
        arrow.addScoreboardTag("Clean");
        ar.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
        vector.rotateAroundX(Math.PI/(24+Math.random()*6)* Misc.RandomPositiveMinus());
        vector.rotateAroundZ(Math.PI/(24+Math.random()*6)*Misc.RandomPositiveMinus());
        vector.rotateAroundY(Math.PI/(30+Math.random()*6)* Misc.RandomPositiveMinus());
        arrow.setVelocity(vector);
        arrow.addScoreboardTag("Blade_Barrage");
        arrow.setInvulnerable(true);
        arrow.setKnockbackStrength(0);
        arrow.setGravity(true);
        Misc.aTrack(arrow, Blade_Barrage_Listener.Track_Radius,100,0,0.03,2,true,true,0);
    }
    private Arrow Spawn_Arrow(Player p,boolean is_left){
        Arrow ar=(Arrow)p.getWorld().spawnEntity(p.getEyeLocation(),EntityType.ARROW);
        ar.addScoreboardTag("Clean");
        ar.setShooter(p);
        ar.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
        ar.addScoreboardTag("Blade_Barrage");
        ar.setInvulnerable(true);
        ar.setKnockbackStrength(0);
        ar.setGravity(true);
        ar.setVelocity(p.getEyeLocation().getDirection().multiply(1.5).rotateAroundY((is_left?1:-1)*Math.PI / (6*Math.random()+12)));
        Misc.aTrack(ar, Blade_Barrage_Listener.Track_Radius,100,0,0.03,2,true,true,0);
        return ar;
    }

    public void Silence_and_Squall(Player p){
        if(add_User_List(p)){
            return;
        }

        p.setGravity(false);
        p.setVelocity(p.getVelocity().multiply(0.1f));


        BukkitRunnable Throw=new BukkitRunnable() {
            boolean Throw=false;
            @Override
            public void run() {
//                Snowball sb=(Snowball) p.getWorld().spawnEntity(p.getEyeLocation(),EntityType.SNOWBALL);
//                ArmorStand as=(ArmorStand)p.getWorld().spawnEntity(sb.getLocation(),EntityType.ARMOR_STAND);
                ArmorStand as=(ArmorStand)p.getWorld().spawnEntity(p.getEyeLocation(),EntityType.ARMOR_STAND);
                as.addScoreboardTag("Clean");
                as.setInvulnerable(true);
                as.setVisible(false);
                as.setBasePlate(false);
                as.addScoreboardTag("No_Break");
                as.addScoreboardTag(p.getUniqueId().toString());
                as.getEquipment().setItemInMainHand(new ItemStack(Material.DIAMOND_PICKAXE));
                as.setCollidable(false);
                as.setVelocity(p.getLocation().getDirection().multiply(1.5f));

                if(!Throw){
                    as.addScoreboardTag("Silence_and_Squall_1");
                    as.setRotation(p.getLocation().getPitch(),0);
                    as.teleport(as.getLocation().add(0,p.getEyeHeight()/2,0).setDirection(p.getLocation().getDirection()));
                    as.setRightArmPose(new EulerAngle(3.65,0,0));
                    Silence_and_Squall_Listener.Rotate(as,p,false);
                }else{
                    as.addScoreboardTag("Silence_and_Squall_2");
                    as.setRightArmPose(new EulerAngle(3.10,3.12,1.54));
                    Silence_and_Squall_Listener.Rotate(as,p,true);
                }
                if(Throw){
                    remove_User_List(p);
                    p.setGravity(true);
                    this.cancel();
                }
                Throw=true;
            }
        };
        Throw.runTaskTimer(DestinyMain.getPlugin(DestinyMain.class),15,15);

    }

    public void Burning_Maul(Player p){
        if(add_User_List(p)||Burning_Maul_Listener.add(p)){
            return;
        }
        Player_Equipment.put(p,new Equipments(p));
        p.getEquipment().setItemInMainHand(Items.getBurning_Big_Axe());
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,300, p.hasPotionEffect(PotionEffectType.SPEED)?p.getPotionEffect(PotionEffectType.SPEED).getAmplifier()+1:0,false));
    }

    public void Hammer_of_Sol(Player p){
        if(add_User_List(p)){
            return;
        }
        Player_Equipment.put(p,new Equipments(p));
        p.getEquipment().setItemInMainHand(Items.getHammer_of_SolAxe());
    }

    public void Storm_Trance(Player p){
        if(add_User_List(p)){
            return;
        }
        Storm_Trance_Listener.add(p);
        p.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
        Misc.Make_Explode(p,Storm_Trance_Listener.Storm_Explode_Range);
        Summon.summon_Lightning(p.getLocation(),Storm_Trance_Listener.Storm_Lightning_Range,Storm_Trance_Listener.Storm_Lightning_Times);
        int[] rgb={0,255,255};
        BukkitRunnable br=new BukkitRunnable() {
            int time=0;
            @Override
            public void run() {
                Summon.summon_Redstone_Particle(p.getLocation(),Storm_Trance_Listener.Storm_Attack_Range[0],48,1,false,rgb);
                if(time>Storm_Trance_Listener.Storm_Time/4||p.isDead()){
                    remove_User_List(p);
                    Storm_Trance_Listener.remove(p);
                    this.cancel();
                }
                time++;
            }
        };
        br.runTaskTimer(DestinyMain.getPlugin(DestinyMain.class),10,5);
    }


//    public void Chaos_Reach(Player p){
//        if(add_User_List(p)){
//            return;
//        }
//
//        if (!lasers.containsKey(p)) return;
//
//        Chaos_Reach.LaserRunnable run = lasers.get(p);
//        if (run.loading != Chaos_Reach.LaserRunnable.LOADING_TIME) return;
//        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2, 1);
//        run.loading = 0;
//
//        for (Block blc : p.getLineOfSight(null, Chaos_Reach.LaserRunnable.RANGE / 2)){
//            for (Entity en : p.getWorld().getNearbyEntities(blc.getLocation(), 1, 1, 1)){
//                if (en instanceof Player) continue;
//                if (en instanceof LivingEntity){
//                    ((LivingEntity) en).damage(20, p);
//                    en.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, en.getLocation(), 4, 1, 1, 1, 0.1);
//                }
//            }
//        }
//        p.getWorld().spawnParticle(Particle.SMOKE_LARGE, run.laser.getEnd(), 5);
//        try {
//            run.laser.callColorChange();
//        }catch (ReflectiveOperationException e1) {
//            e1.printStackTrace();
//        }
//        p.setGravity(false);
//        p.setVelocity(p.getVelocity().zero());
//
//    }
}