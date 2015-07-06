

import mc.alk.arena.competition.Competition;
import mc.alk.arena.controllers.PlayerController;
import mc.alk.arena.objects.ArenaPlayer;
import mc.alk.arena.objects.modules.ArenaModule;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

/**
 * 
 * @author Nikolai
 */
public class StopInterference extends ArenaModule {
    
    String name = "StopInterference";
    String version = "1.0";
    String author = "Europia79";
    
    String[] description = {
        "SourcePlayer & TargetPlayer must be in the same Arena.",
        "If not, cancel the damage."
    };
    
    /**
     * SourcePlayer & TargetPlayer must be in the same Arena, 
     * otherwise, cancel the damage.
     */
    @EventHandler (priority=EventPriority.MONITOR)
    public void onProjectileDamage(EntityDamageByEntityEvent e) {
        Entity eTarget = e.getEntity();
        if (!(eTarget instanceof Player)) return;
        Player pTarget = (Player) eTarget;
        if (e.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) e.getDamager();
            ProjectileSource source = projectile.getShooter();
            if (source instanceof Player) {
                Player pSource = (Player) source;
                ArenaPlayer apTarget = PlayerController.toArenaPlayer(pTarget);
                ArenaPlayer apSource = PlayerController.toArenaPlayer(pSource);
                
                if (differentArenas(pTarget, pSource)) {
                    e.setDamage(0.0);
                    e.setCancelled(true);
                }
                
            }
        } else if (e.getDamager() instanceof Player) {
            Player damager = (Player) e.getDamager();
            if (differentArenas(pTarget, damager)) {
                e.setDamage(0.0);
                e.setCancelled(true);
            }
        }
    }
    
    public boolean differentArenas(Player p1, Player p2) {
        return ( !(sameArenas(p1, p2)));
    }
    
    public boolean sameArenas(Player p1, Player p2) {
        ArenaPlayer ap1 = PlayerController.toArenaPlayer(p1);
        ArenaPlayer ap2 = PlayerController.toArenaPlayer(p2);
        Competition comp1 = ap1.getCompetition();
        Competition comp2 = ap2.getCompetition();
        if (comp1 != null && comp1.equals(comp2)) {
            return true;
        } else if (comp1 == null && comp2 == null) {
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getVersion() {
        return this.version;
    }

}
