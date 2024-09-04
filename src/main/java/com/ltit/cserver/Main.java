package com.ltit.cserver;

import com.ltit.cserver.commands.GamemodeCommand;
import com.ltit.cserver.commands.InfoCommand;
import com.ltit.cserver.commands.SpawnCommand;
import com.ltit.cserver.commands.TpCommand;
import de.articdive.jnoise.generators.noisegen.opensimplex.SuperSimplexNoiseGenerator;
import de.articdive.jnoise.pipeline.JNoise;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.player.*;
import net.minestom.server.instance.*;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.event.item.PickupItemEvent;
import net.minestom.server.world.Difficulty;

import java.time.Duration;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        // Initialize the server
        MinecraftServer minecraftServer = MinecraftServer.init();
        // Register Events (set spawn instance, teleport player at spawn)
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();

        // Set the ChunkGenerator
        instanceContainer.setGenerator(unit -> {
            // unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK);
            Long lowLimit = 5L;
            Long highLimit = Long.MAX_VALUE - 5L;
            Long seed = lowLimit + (long) (Math.random() * (highLimit - lowLimit));
            // FastSimplexNoiseGenerator fastSimplexNoiseGenerator = new FastSimplexNoiseGenerator(seed, Simplex2DVariant.IMPROVE_X);
            JNoise noise = JNoise.newBuilder()
                    .superSimplex(SuperSimplexNoiseGenerator.newBuilder())
                    .scale(0.01)
                    .addModifier(v -> v * 20)
                    .build();
            Point start = unit.absoluteStart();
            Point size = unit.size();
            Random random = new Random();
            for (int xo = 0; xo < size.x(); xo++){
                for (int zo = 0; zo < size.z(); zo++){
                    int x = (int) (start.x() + xo);
                    int z = (int) (start.z() + zo);
                    double height = noise.evaluateNoise(x, z);
                    if (height < -64) height = -60;
                    height = height +  70;
                    for (int y = (int) start.y(); y < start.y() +size.y(); y++){
                        if (y == size.y()){
                            unit.modifier().setBlock(x, y, z, Block.BEDROCK);
                            continue;
                        }
                        if (y <= -64){
                            unit.modifier().setBlock(x, -64, z, Block.BEDROCK);
                            continue;
                        }
                        else{
                            if(y <= height){
                                if(y <= 90){
                                    if(y >= height-1){
                                        if(y < 59){
                                            unit.modifier().setBlock(x, y, z, Block.SAND);
                                        }
                                        else {
                                            unit.modifier().setBlock(x, y, z, Block.GRASS_BLOCK);
                                        }
                                    } else if (y > height - 6) {
                                        unit.modifier().setBlock(x, y, z, Block.DIRT);
                                    } else if (y > 0) {
                                        unit.modifier().setBlock(x, y, z, Block.STONE);
                                    } else{
                                        unit.modifier().setBlock(x, y, z, Block.DEEPSLATE);
                                    }
                                }
                                else{
                                    unit.modifier().setBlock(x, y, z, Block.STONE);
                                }
                            }
                            else{
                                if(y<59){
                                    unit.modifier().setBlock(x, y, z, Block.WATER);
                                }
                            }
                        }
                        if(y >= height && y <= height + 1){
                            var nextFeature = random.nextFloat();
                            if(nextFeature <= 0.1 && y > 59 && y < 90){
                                unit.modifier().setBlock(x, y, z, Block.SHORT_GRASS);
                            }
                            if(nextFeature >=0.875 && nextFeature < 0.9 && y > 59 && y < 90){
                                var flowerRandom = random.nextFloat();
                                if(flowerRandom <= 0.25){
                                    unit.modifier().setBlock(x, y, z, Block.POPPY);
                                }
                                else if (flowerRandom > 0.25 && flowerRandom <= 0.5){
                                    unit.modifier().setBlock(x, y, z, Block.ALLIUM);
                                }
                                else if(flowerRandom > 0.5 && flowerRandom <= 0.75){
                                    unit.modifier().setBlock(x, y, z, Block.ORANGE_TULIP);
                                }
                                else {
                                    unit.modifier().setBlock(x, y, z, Block.CORNFLOWER);
                                }
                            }
                            if(nextFeature >=0.85 && nextFeature < 0.9 && y < 59){
                                unit.modifier().setBlock(x, y, z, Block.SEAGRASS);
                            }
                            /*
                            if(random.nextFloat() >= 0.9975 && y > 59 && y < 90){
                                int treeHeight = 5;
                                for(int i = 0; i < treeHeight; i++){
                                    for(int xt = 0; xt < 5; xt++){
                                        for(int zt = 0; zt < 5; zt++){
                                            if(i > 1 && i < treeHeight-1){
                                                unit.modifier().setBlock(x-2+xt, y+i, z-2+zt, Block.OAK_LEAVES);
                                            }
                                        }
                                    }
                                    unit.modifier().setBlock(x, y+i, z, Block.OAK_LOG);
                                }
                                unit.modifier().setBlock(x, y+treeHeight, z, Block.OAK_LEAVES);

                            }*/
                        }

                    }
                }
            }
        });

        // Light the chunks
        instanceContainer.setChunkSupplier(LightingChunk::new);

        // Add an event callback to specify the spawning instance (and the spawn position)
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            final Player player = event.getPlayer();
            PlayerSkin skinFromUsername = PlayerSkin.fromUsername(player.getUsername());
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(0, 102, 0));
            player.setSkin(skinFromUsername);
        });

        globalEventHandler.addListener(PlayerSkinInitEvent.class, event -> {
            PlayerSkin skin = PlayerSkin.fromUsername(event.getPlayer().getUsername());
            event.setSkin(skin);
            Audiences.server().sendMessage(Component.text(String.format("Welcome, %s!", event.getPlayer().getUsername())));
        });

        //Check for block breaks
        globalEventHandler.addListener(PlayerBlockBreakEvent.class, event ->{
            if(event.getPlayer().getGameMode() == GameMode.SURVIVAL || event.getPlayer().getGameMode() == GameMode.ADVENTURE){
                BlockVec blockPos = event.getBlockPosition();
                var material = event.getBlock().registry().material();
                if(material != null){
                    var itemStack = ItemStack.of(material);
                    ItemEntity itemEntity = new ItemEntity(itemStack);
                    itemEntity.setInstance(event.getInstance(), new Pos(blockPos.blockX()+0.5, blockPos.blockY()+0.5, blockPos.blockZ()+0.5));
                    itemEntity.setPickupDelay(Duration.ofMillis(10));
                }
            }
        });
        globalEventHandler.addListener(PickupItemEvent.class, event ->{
            if(event.getLivingEntity() instanceof Player player){
                player.getInventory().addItemStack(event.getItemStack());
            }
        });
        globalEventHandler.addListener(PlayerTickEvent.class, event ->{
            if(event.getPlayer().getPosition().blockY() < -100){
                if(event.getPlayer().isInvulnerable()){
                    event.getPlayer().setInvulnerable(false);
                    event.getPlayer().damage(DamageType.OUT_OF_WORLD, Integer.MAX_VALUE);
                    event.getPlayer().setInvulnerable(true);
                }
                else{
                    event.getPlayer().damage(DamageType.OUT_OF_WORLD, Integer.MAX_VALUE);
                }
            }

        });
        globalEventHandler.addListener(ItemDropEvent.class, event -> {
            ItemEntity itemEntity = new ItemEntity(event.getItemStack());
            itemEntity.setInstance(instanceContainer, event.getPlayer().getPosition());
            if(event.getPlayer().isSneaking()){
                itemEntity.setVelocity(event.getPlayer().getPosition().add(0, 1, 0).direction().mul(25));
            }else{
                itemEntity.setVelocity(event.getPlayer().getPosition().add(0, 1, 0).direction().mul(10));
            }
            itemEntity.setPickupDelay(Duration.ofMillis(500));
        });
        globalEventHandler.addListener(PlayerDeathEvent.class, event -> {
            instanceContainer.getPlayers().forEach(x -> {
                x.sendMessage(event.getPlayer().getUsername() + " Has died. RIP");
            });
        });

        MinecraftServer.getCommandManager().register(new InfoCommand());
        MinecraftServer.getCommandManager().register(new GamemodeCommand());
        MinecraftServer.getCommandManager().register(new SpawnCommand());
        MinecraftServer.getCommandManager().register(new TpCommand());

        MinecraftServer.setDifficulty(Difficulty.NORMAL);

        var scheduler = MinecraftServer.getSchedulerManager();
        scheduler.buildShutdownTask(() -> {
            System.out.println("Server shutting down");
            instanceContainer.saveChunksToStorage();
        });


        // Start the server
        minecraftServer.start("0.0.0.0", 25565);

    }
}