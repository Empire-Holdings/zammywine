package zamwine;

import org.powerbot.script.*;
import org.powerbot.script.rt4.GroundItem;
import org.powerbot.script.rt4.Magic;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Bank.Amount;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.Inventory;
import org.powerbot.script.rt4.Game;
import org.powerbot.script.rt4.Widgets;
import org.powerbot.script.rt4.Widget;

import java.awt.*;
import java.util.concurrent.Callable;


@Script.Manifest(name = "winegrab", description = "wine n dine")

public class Powergrab extends PollingScript<ClientContext> implements MessageListener, PaintListener {
	int wineId = 245; //245
	int teleId = 8009;
	private boolean tele = false;
	GroundItem wine;
	private GameObject booth;
	private static final int BANKBOOTHID = 24101;
	private static final Tile WINE_TILE = new Tile(2932,3515, 0);
	private static final Area BANK_FIRST_AREA = new Area(new Tile(2950, 3380), new Tile(2954, 3376));
	private static final Area BANK_SECOND_AREA = new Area(new Tile(2943, 3371), new Tile(2947, 3368));
	private static final Area WINE_FIRST_AREA = new Area(new Tile(2959, 3384), new Tile(2964, 3380));
	private static final Area WINE_SECOND_AREA = new Area(new Tile(2963, 3397), new Tile(2967, 3394));
	private static final Area WINE_THIRD_AREA = new Area(new Tile(2963, 3404), new Tile(2960, 3407));
	private static final Area WINE_FOURTH_AREA = new Area(new Tile(2954, 3418), new Tile(2950, 3422));
	private static final Area WINE_FIFTH_AREA = new Area(new Tile(2950, 3435), new Tile(2946, 3439));
	private static final Area WINE_SIXTH_AREA = new Area(new Tile(2947, 3452), new Tile(2943, 3455));
	private static final Area WINE_SEVENTH_AREA = new Area(new Tile(2946, 3469), new Tile(2943, 3472));
	private static final Area WINE_EIGHTH_AREA = new Area(new Tile(2944, 3486), new Tile(2941, 3489));
	private static final Tile WINE_NINTH_TILE = new Tile(2941, 3503, 0);
	private static final Area TELEPORT_AREA = new Area(new Tile(2961, 3384), new Tile(2969, 3375));
	
	
	@Override
	public void poll() {
		if ((!ctx.groundItems.select().id(wineId).isEmpty()) && (!tele)) {
			
			System.out.println("grabbing");
			ctx.magic.cast(Magic.Spell.TELEKINETIC_GRAB);
			wine = ctx.groundItems.nearest().poll();
			wine.interact(false, "Cast", "Telekinetic Grab -> Wine of zamorak");
			wine = null;
			
			ctx.game.tab(Game.Tab.INVENTORY);
			Condition.sleep((int)((Math.random() *200)+600));
			if (ctx.inventory.select().count() == 28) {
				tele = true;
				Condition.wait(new Callable<Boolean>() {
		            @Override
		            public Boolean call() throws Exception {
		            	return (tele == true);
		            }
		        }, 100, 50);
				teleport();
			}
				
		}
	}
	
	@Override
	public void messaged(MessageEvent e) {
		if (e.text().contains("runes to cast this spell")) {
			System.out.println("out");
		} else if (e.text().contains("inventory")) {
			//System.out.println("full");
			//tele = true;
			//teleport();
		}
	}
	
	@Override
	public void repaint(Graphics g) {
		g.setColor(Color.RED);
		if (wine != null) {
			wine.draw(g);
		}
	}
	
	private void teleport() {
		Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
            	return (ctx.game.tab(Game.Tab.INVENTORY));
            }
        }, 100, 50);
		ctx.movement.running(true);
		Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
            	return (ctx.movement.running(true));
            }
        }, 100, 50);
		ctx.inventory.id(teleId).poll().interact("Break");
		Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
            	return TELEPORT_AREA.contains(ctx.players.local().tile());
            }
        }, 100, 50);
		ctx.movement.step(BANK_FIRST_AREA.getRandomTile());
		Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
            	return BANK_FIRST_AREA.contains(ctx.players.local().tile());
            }
        }, 100, 50);
		ctx.movement.step(BANK_SECOND_AREA.getRandomTile());
		Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
            	return BANK_SECOND_AREA.contains(ctx.players.local().tile());
            }
        }, 100, 50);
		bank();
	}
	
	private void bank() {
		System.out.println("bank");
		if (booth == null || !booth.valid()) {
            System.out.println("Getting new banker");
            booth = ctx.objects.select().id(BANKBOOTHID).nearest().peek();
        }
        if (!ctx.bank.opened()) {
            if (!booth.inViewport())
                ctx.camera.turnTo(booth);
            booth.click();
            Condition.wait(new Callable<Boolean>() {
	            @Override
	            public Boolean call() throws Exception {
	                return (ctx.bank.opened());
	            }
	        }, 100, 100);
        }
        if (ctx.bank.opened()) {
        	ctx.bank.deposit(wineId, 26);
        	ctx.bank.close();
        }
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return (ctx.bank.close());
            }
        }, 100, 100);
        walkToWine();
        
	}
	
	private void walkToWine() {
		ctx.movement.step(WINE_FIRST_AREA.getRandomTile());
		Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
            	return WINE_FIRST_AREA.contains(ctx.players.local().tile());
            }
        }, 100, 100);
		ctx.movement.step(WINE_SECOND_AREA.getRandomTile());
		Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
            	return WINE_SECOND_AREA.contains(ctx.players.local().tile());
            }
        }, 100, 100);
		ctx.movement.step(WINE_THIRD_AREA.getRandomTile());
		Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
            	return WINE_THIRD_AREA.contains(ctx.players.local().tile());
            }
        }, 100, 100);
		ctx.movement.step(WINE_FOURTH_AREA.getRandomTile());
		Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
            	return WINE_FOURTH_AREA.contains(ctx.players.local().tile());
            }
        }, 100, 100);
		ctx.movement.step(WINE_FIFTH_AREA.getRandomTile());
		Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
            	return WINE_FIFTH_AREA.contains(ctx.players.local().tile());
            }
        }, 100, 100);
		ctx.movement.step(WINE_SIXTH_AREA.getRandomTile());
		Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
            	return WINE_SIXTH_AREA.contains(ctx.players.local().tile());
            }
        }, 100, 100);
		ctx.movement.step(WINE_SEVENTH_AREA.getRandomTile());
		Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
            	return WINE_SEVENTH_AREA.contains(ctx.players.local().tile());
            }
        }, 100, 100);
		ctx.movement.step(WINE_EIGHTH_AREA.getRandomTile());
		Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
            	return WINE_EIGHTH_AREA.contains(ctx.players.local().tile());
            }
        }, 100, 100);
		ctx.movement.step(WINE_NINTH_TILE);
		Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
            	return (ctx.players.local().tile() == WINE_NINTH_TILE);
            }
        }, 100, 100);
		ctx.movement.step(WINE_TILE);
		Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
            	return (ctx.players.local().tile() == WINE_TILE);
            }
        }, 100, 100);
		tele = false;
		
	}
	
}


