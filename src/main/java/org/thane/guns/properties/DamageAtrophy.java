package org.thane.guns.properties;

import org.thane.guns.Bullet;

public class DamageAtrophy implements BulletProperty {

        private float atrophy = 0.95F;

        public DamageAtrophy() {
        }

        public DamageAtrophy(float atrophy) {
            this.atrophy = atrophy;
        }

        @Override
        public boolean onIteration(Bullet bullet, int iterationCount) {
            bullet.getDamageRange().setMax(bullet.getDamageRange().getMax() * atrophy);
            bullet.getDamageRange().setMin(bullet.getDamageRange().getMin() * atrophy);
            return BulletProperty.super.onIteration(bullet, iterationCount);
        }

        @Override
        public DamageAtrophy clone() {
            try {
                return (DamageAtrophy) super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                return null;
            }
        }

    @Override
    public int getImportance() {
        return 0;
    }
}
