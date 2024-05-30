package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

import static org.junit.jupiter.api.Assertions.*;

class ImageWriterTest {
    /**
     * Test method for {@link renderer.ImageWriter#writeToImage()}.
     */
    @Test
    public void writeImageTest() {
        ImageWriter imageWriter = new ImageWriter("Image1", 800, 500); // create an image
        int nX = imageWriter.getNx(); // rows
        int nY = imageWriter.getNy();// columns
        Color green = new Color(182, 197, 184); // rgb component for green color to background
        Color brown = new Color(147, 144, 127); // rgb component for brown color to lines
        for (int col = 0; col < nY; col++)// move on all columns
        {
            for (int row = 0; row < nX; row++)// move on all rows
            {
                if (col % 50 == 0 || row % 50 == 0)// to create the lines between the squares
                    imageWriter.writePixel(row, col, brown); // drawing brown strips in regular intervals (draw the
                    // lines)
                else
                    imageWriter.writePixel(row, col, green);// draw the backgrownd
            }
        }
        imageWriter.writeToImage(); // finally create this image
    }
}