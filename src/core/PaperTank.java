package core;

public class PaperTank {
    private int paperCount;
    private static final int MAXIMUM_CAPACITY = 500;
    private static final int LOW_PAPER_THRESHOLD = 50;
    private boolean lowPaperFlag;

    public PaperTank(int paperCount) {
        this.paperCount = Math.min(paperCount, MAXIMUM_CAPACITY);
        this.lowPaperFlag = false;
    }

    public boolean hasPaper() {
        return paperCount > 0;
    }

    public boolean isEmpty() {
        return paperCount == 0;
    }

    public boolean useOne() {
        if (paperCount > 0) {
            paperCount--;
            if (paperCount < LOW_PAPER_THRESHOLD) {
                lowPaperFlag = true;
            }
            return true;
        }
        return false;
    }

    public void refill(int amount) {
        paperCount = Math.min(paperCount + amount, MAXIMUM_CAPACITY);
        lowPaperFlag = false;
    }

    public int getPaperCount() {
        return paperCount;
    }

    public int getSheets() {
        return paperCount;
    }

    public boolean isLowOnPaper() {
        return paperCount < LOW_PAPER_THRESHOLD;
    }

    public void flagLowPaper() {
        lowPaperFlag = true;
    }

    public boolean getLowPaperFlag() {
        return lowPaperFlag;
    }

    public int getCapacityPercentage() {
        return (int) ((paperCount / (double) MAXIMUM_CAPACITY) * 100);
    }

    public void displayStatus() {
        System.out.println("Paper Tank Status: " + paperCount + "/" + MAXIMUM_CAPACITY + 
                          " sheets (" + getCapacityPercentage() + "%)");
        if (isEmpty()) {
            System.out.println("  WARNING: OUT OF PAPER");
        } else if (isLowOnPaper()) {
            System.out.println("  WARNING: Low paper level");
        }
    }
}
