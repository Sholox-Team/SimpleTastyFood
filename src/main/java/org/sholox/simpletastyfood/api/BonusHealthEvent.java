package org.sholox.simpletastyfood.api;

import net.neoforged.bus.api.Event;

public class BonusHealthEvent extends Event {
    public static class Pre extends BonusHealthEvent {
        public Pre(float bonusHealth) {
            super(bonusHealth);

        }
    }

    public static class Post extends BonusHealthEvent {
        public Post(float bonusHealth) {
            super(bonusHealth);
        }
    }

    public BonusHealthEvent(float bonusHealth) {
        this.bonusHealth = bonusHealth;
    }

    public float bonusHealth;
}
