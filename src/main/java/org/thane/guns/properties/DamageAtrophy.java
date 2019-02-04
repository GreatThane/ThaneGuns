package org.thane.guns.properties;

import org.thane.guns.Bullet;

public class DamageAtrophy implements BulletProperty {

        private float atrophy = 0.05F;

        public DamageAtrophy() {
        }

        public DamageAtrophy(float atrophy) {
            this.atrophy = atrophy;
        }

        @Override
        public boolean onIteration(Bullet bullet, int iterationCount) {
            bullet.getDamageRange().setMax(bullet.getDamageRange().getMax() * atrophy);
            bullet.getDamageRange().setMin(bullet.getDamageRange().getMin() * atrophy);
            return false;
        }

        @Override
        public DamageAtrophy clone() throws CloneNotSupportedException {
            return (DamageAtrophy) super.clone();
        }

    @Override
    public int getImportance() {
        return 0;
    }
}
