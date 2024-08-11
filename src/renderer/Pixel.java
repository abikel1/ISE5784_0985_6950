package renderer;

public class Pixel {
    private long maxMunRows;
    private long maxMunCols;
    private long pixels;
    public volatile int row = 0;
    public volatile int col = -1;
    private long counter = 0;
    private int percent = 0;
    private long nextCounter = 0;
    private boolean print;

    // Constructor
    public Pixel(int maxRows, int maxCols, boolean print) {
        this.maxMunRows = maxRows;
        this.maxMunCols = maxCols;
        this.pixels = maxMunCols * maxMunRows;
        this.nextCounter = pixels / 100;
        this.print = print;
        if (print) {
            System.out.printf("\r %02d%%", percent);
        }
    }

    public Pixel() {}

    // Method to move to the next pixel
    public boolean nextPixel(Pixel p) {
        int newPercent = nextp(p);
        if (newPercent > percent && print) {
            percent = newPercent;
            synchronized (System.out) {
                System.out.printf("\r %02d%%", percent);
            }
        }
        if (newPercent >= 0) return true;
        if (print) {
            synchronized (System.out) {
                System.out.printf("\r %02d%%", 100);
            }
        }
        return false;
    }

    // Synchronized method to calculate the next pixel and update the progress percentage
    private synchronized int nextp(Pixel target) {
        col++;
        counter++;
        if (col < maxMunCols) {
            target.row = this.row;
            target.col = this.col;
            if (counter == nextCounter) {
                percent++;
                nextCounter = pixels * (percent + 1) / 100;
                return percent;
            }
            return 0;
        }
        row++;
        if (row < maxMunRows) {
            col = 0;
            if (counter == nextCounter) {
                percent++;
                nextCounter = pixels * (percent + 1) / 100;
                return percent;
            }
            return 0;
        }
        return -1;
    }
}
