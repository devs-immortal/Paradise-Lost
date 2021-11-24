package net.id.aether.util;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class MetaballManager {

    public static class MetaballGenerator {

        private final Random random; // random for generating
        private final int generatorAreaSize; // total area the balls could be generated within
        private int ballAmount; // amount of balls to generate
        private int ballMinSize; // minimum radius a ball could have
        private int ballMaxSize; // maximum radius a ball could have
        private int ballMultiplier; // yup 🥴

        MetaballGenerator(Random random, int area, int ballAmount, int ballMinSize, int ballMaxSize, int ballMultiplier) {
            this.random = random;
            this.generatorAreaSize = area;
            this.ballAmount = ballAmount;
            this.ballMinSize = ballMinSize;
            this.ballMaxSize = ballMaxSize;
            this.ballMultiplier = ballMultiplier;
        }

        // Constructor, defaults multiplier to 1, this is porbably what you should generally use
        MetaballGenerator(Random random, int area, int ballAmount, int ballMinSize, int ballMaxSize) {
            this(random, area, ballAmount, ballMinSize, ballMaxSize, 1);
        }


        public double[][] generate() {
            // create the metaballs
            Metaball[] balls = new Metaball[ballAmount];
            for (int i = 0; i < ballAmount; i++) {
                int randRadius;
                if (ballMaxSize > ballMinSize) { // if range is valid, makes them within the provided range
                    randRadius = random.nextInt(ballMaxSize - ballMinSize) + ballMinSize;
                } else { // otherwise they're all just the max size, TODO throw an error for invalid sizes
                    randRadius = ballMaxSize;
                }
                // defines an origin, and moves it in a bit so that the ball doesn't go off the edges
                int randX = random.nextInt(generatorAreaSize-4*randRadius)+2*randRadius;
                int randY = random.nextInt(generatorAreaSize-4*randRadius)+2*randRadius;
                balls[i] = new Metaball(randX, randY, randRadius, ballMultiplier); // actually makes the metaball
            }
            // generate values
            double[][] coords = new double[generatorAreaSize][generatorAreaSize];
            double maximum = 0; // calculates a maximum to fix center points
            for (int y = 0; y < generatorAreaSize; y++) {
                for (int x = 0; x < generatorAreaSize; x++) {
                    double value = 0;
                    for (Metaball ball : balls) {
                        value += ball.scaledDistanceToPoint(x, y);
                    }
                    coords[y][x] = value;
                    if (maximum < value) maximum = value;
                }
            }
            for (int y = 0; y < generatorAreaSize; y++) {
                for (int x = 0; x < generatorAreaSize; x++) { // TODO if value is sub-zero, it's a center, but this is really inefficient
                    if (coords[y][x] < 0) {
                        coords[y][x] = maximum;
                    }
                }
            }
            return coords; // return 2D array of values
        }


    }

    /**
     *  Testing method for metaballs, runs a generator with
     *  options, then creates a GUI to change some settings
     *  around to try different options.
     *
     *  TODO:
     *   > Distribution functions for balls
     *   > More stringy connection algorithm
     *   > Figure out what to do with the multiplier
     */
    public static void main(String[] args) {
        MetaballGenerator gen = new MetaballGenerator(new Random(), 64, 4, 3, 5);
        new Displayer(gen);
    }

    // This is very poorly formatted, sucks to suck
    private static class Displayer extends JFrame {

        JLabel imageLabel;
        JSlider thresholdSlider;
        MetaballGenerator generator;
        double[][] lastGen;
        int width;

        Displayer(MetaballGenerator generator) {
            this.generator = generator;
            double[][] initialGen = generator.generate();
            lastGen = initialGen;
            BufferedImage img = arrayToBufferedImage(initialGen, 1);
            width = img.getWidth();
            setSize(width*10, img.getHeight()*10+160);
            setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
            setTitle(width+"x"+width+" Metaball grid");

            // IMAGE PANEL
            imageLabel = new JLabel();
            imageLabel.setIcon(new ImageIcon(img.getScaledInstance(width*9, img.getHeight()*9, Image.SCALE_FAST)));

            JButton b = new JButton();
            b.setText("Generate new grid");
            b.setSize(width*10, 60);
            b.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    setImage(generator.generate());
                }
            });

            JPanel imagePanel = new JPanel();
            imagePanel.add(imageLabel);
            imagePanel.add(b);
            // IMAGE PANEL END

            JPanel thresholdPanel = makeSliderPanel(new JSlider(), 0, 1000, 1000, "Threshold", new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    setImage(lastGen);
                }
            }, 1000);
            thresholdSlider = ((JSlider) thresholdPanel.getComponent(1));

            JSlider amountSlider = new JSlider();
            JPanel amountPanel = makeSliderPanel(amountSlider, 2, 20, generator.ballAmount, "Ball Amount", new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    generator.ballAmount = amountSlider.getValue();
                    setImage(generator.generate());
                }
            }, 1);

            JSlider lowerSizeSlider = new JSlider();
            JPanel lowerSizePanel = makeSliderPanel(lowerSizeSlider, 2, 12, generator.ballMinSize, "Minimum Ball Size", new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    generator.ballMinSize = lowerSizeSlider.getValue();
                    setImage(generator.generate());
                }
            }, 1);

            JSlider upperSizeSlider = new JSlider();
            JPanel upperSizePanel = makeSliderPanel(upperSizeSlider, 2, 12, generator.ballMaxSize, "Maximum Ball Size", new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    generator.ballMaxSize = upperSizeSlider.getValue();
                    setImage(generator.generate());
                }
            }, 1);

            JSlider multiplierSlider = new JSlider();
            JPanel multiplierPanel = makeSliderPanel(multiplierSlider, 1, 8, generator.ballMultiplier, "Ball Scale Multiplier", new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    generator.ballMultiplier = multiplierSlider.getValue();
                    setImage(generator.generate());
                }
            }, 1);

            add(imagePanel);
            add(b);
            add(thresholdPanel);
            add(amountPanel);
            add(lowerSizePanel);
            add(upperSizePanel);
            add(multiplierPanel);

            setVisible(true);
            setDefaultCloseOperation(EXIT_ON_CLOSE);

        }

        private JPanel makeSliderPanel(JSlider newSlider, int min, int max, int start, String title, ChangeListener listener, double divisor) {
            newSlider.setSize(width*10, 60);
            newSlider.setMinimum(min);
            newSlider.setMaximum(max);
            newSlider.setValue(start);
            JLabel newLabel = new JLabel(title + ": " + (((double) newSlider.getValue())/divisor));
            newSlider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    newLabel.setText(title + ": " + (((double) newSlider.getValue())/divisor));
                }
            });
            newSlider.addChangeListener(listener);

            JPanel newPanel = new JPanel();
            newPanel.add(newLabel);
            newPanel.add(newSlider);
            return newPanel;
        }

        private void setImage(double[][] pixels) {
            lastGen = pixels;
            BufferedImage img = arrayToBufferedImage(pixels, (((double) thresholdSlider.getValue())/1000));
            imageLabel.setIcon(new ImageIcon(img.getScaledInstance(width*9, img.getHeight()*9, Image.SCALE_FAST)));
        }

        private BufferedImage arrayToBufferedImage(double[][] imgArray, double threshold) {
            BufferedImage img = new BufferedImage(imgArray[0].length, imgArray.length, BufferedImage.TYPE_INT_RGB);
            for (int y = 0; y < imgArray[0].length; y++) {
                for (int x = 0; x < imgArray.length; x++) {
                    float brightness;
                    if (imgArray[y][x] > threshold) {
                        brightness = 1;
                    } else {
                        brightness = 0;
                    }
                    //float brightness = (float) Math.max(0, Math.min(1, imgArray[y][x]));
                    img.setRGB(x, y, new Color(brightness, brightness, brightness).getRGB());
                }
            }
            return img;
        }
    }

    // Metaball class for use in the generator
    private static class Metaball {

        Point2D center; // center of the metaball
        int radius; // radius of the metaball
        int multiplier; // yup

        // Create a new metaball at a location with a specified radius and multiplier
        Metaball(int x, int y, int radiusIn, int multiplierIn) {
            this.center = new Point2D.Double(x, y);
            this.radius = radiusIn;
            this.multiplier = multiplierIn;
        }

        // calculate the inverse scaled distance to a different point
        public double scaledDistanceToPoint(int otherX, int otherY) {
            double dist = this.center.distance(otherX, otherY); // raw distance
            if (dist == 0) {
                return 1; // if center, avoid division by zero
            } else {
                return (radius)/(dist*multiplier); // otherwise calculate inverse by radius
            }
        }

    }
}
