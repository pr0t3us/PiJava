/*
 * Copyright (C) 2016-2017 Jim Darby.
 *
 * This software is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, If not, see
 * <http://www.gnu.org/licenses/>.
 */

package Boards.com.pimoroni;

import Devices.APA102;
import Graphics.Colour;
import Graphics.ColourMatrix;
import Graphics.ColourMatrixDemo;
import Graphics.Point;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.RaspiPin;

import java.io.IOException;

/**
 * This class drives a Pimoroni Blinkt LED string.
 *
 * @author Jim Darby
 */
public class Blinkt implements ColourMatrix
{
    public Blinkt ()
    {
        a = new APA102 (GpioFactory.getInstance(), RaspiPin.GPIO_04, RaspiPin.GPIO_05, 8);
    }
    
   /**
     * Set a pixel the generic way.
     * 
     * @param p The pixel to set.
     * @param value The colour to set it to.
     */
    @Override
    public void setPixel(Point p, Colour value)
    {
        if (p.getY () != 0)
            throw new IllegalArgumentException ("Invalid Y coordinate");
        
        set (p.getX (), value.getRed (), value.getGreen (), value.getBlue (), 31);
    }
    
   /**
     * Set a LED to a specific red, green and blue value. We also set the
     * brightness.
     * 
     * @param n The LED number, in the range 0 to the number of LEDs minus one.
     * @param r The red value (0 to 255).
     * @param g The green value (0 to 255).
     * @param b The blue value (0 to 255).
     * @param bright The brightness (0 to 31).
     */
    public void set (int n, int r, int g, int b, int bright)
    {
        a.set (n, r, g, b, bright);
    }
    
    /**
     * Update the LED chain.
     */
    @Override
    public final void show ()
    {
        a.show ();
    }
    
    /**
     * Return a point with the maximum values for X and Y in this
     * matrix.
     * 
     * @return The maximum size.
     */
    @Override
    public Point getMax ()
    {
        return MAX;
    }
    
    /**
     * Run a simple test demo on the board.
     * 
     * @param args The command line arguments. They're ignored.
     * 
     * @throws InterruptedException If Thread.sleep gets interrupted.
     * @throws java.io.IOException In case of trouble.
     */
    public static void main (String args[]) throws InterruptedException, IOException
    {
        final Blinkt b = new Blinkt ();
        
        ColourMatrixDemo.run (b);
    }
    
    /** The width of the board. */
    public static final int WIDTH = 8;
    /** The height of the board. */
    public static final int HEIGHT = 1;
    /** The maximum X value. */
    public static final int MAX_X = WIDTH - 1;
    /** The maximum Y value. */
    public static final int MAX_Y = HEIGHT - 1;
    
    /** The maximum values as a Point. */
    private final static Point MAX = new Point (MAX_X, MAX_Y);
    
    /** Internal pointer to the hat. */
    private final APA102 a;
}
