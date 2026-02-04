package core;

public class PaperTank {

    private int paperCount;

    public PaperTank(int paperCount) {
        this.paperCount = paperCount;
    }

    public boolean hasPaper() {
        return paperCount > 0;
    }

    public boolean useOne() {
        if (paperCount > 0) {
            paperCount--;
        }
        return false;
    }

    public boolean isEmpty() {
        return paperCount == 0;
    }

    public void refill(int amount) {
        paperCount += amount;
    }

    public int getPaperCount() {
        return paperCount;
    }


    public int getSheets() {
        return this.paperCount;
    }
}
