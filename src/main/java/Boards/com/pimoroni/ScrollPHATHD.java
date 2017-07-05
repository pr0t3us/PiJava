/*
 * Copyright (C) 2017 Jim Darby.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.  If not, see
 * <http://www.gnu.org/licenses/>.
 */

package Boards.com.pimoroni;

import Devices.IS31FL3731;
import Graphics.MonoMatrix;
import Graphics.MonoMatrixDemo;
import Graphics.Point;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;


/**
 * This class controls a Pimoroni ScrollpHAT HD.
 *
 * @author Jim Darby
 */
public class ScrollPHATHD implements MonoMatrix {
    /**
     * Device width.
     */
    public final static int WIDTH = 17;
    /**
     * Device height.
     */
    public final static int HEIGHT = 7;
    /**
     * The maximum X value.
     */
    public final static int MAX_X = WIDTH - 1;
    /**
     * The maximum Y value.
     */
    public final static int MAX_Y = HEIGHT - 1;

    /**
     * The maximum values as a Point.
     */
    private final static Point MAX = new Point(MAX_X, MAX_Y);

    /**
     * The device itself.
     */
    private final IS31FL3731 phat;
    /**
     * Flag to flip the x coordinate.
     */
    private boolean flip_x = false;
    /**
     * Flag to flip the y coordinate.
     */
    private boolean flip_y = false;

    /**
     * Create a ScrollPHATHD object.
     *
     * @throws IOException                              In case of issues.
     * @throws I2CFactory.UnsupportedBusNumberException In case it can't find
     *                                                  the correct I2C bus.
     * @throws java.lang.InterruptedException           In case of issues.
     */
    public ScrollPHATHD() throws IOException, I2CFactory.UnsupportedBusNumberException, InterruptedException {
        phat = new IS31FL3731(I2CFactory.getInstance(I2CBus.BUS_1), 0x74);
    }

    /**
     * Update the displayed data. Call this after setting up what you want
     * displayed and it will transfer it to the device and hence actually
     * display it.
     *
     * @throws IOException In case of problems.
     */
    @Override
    public void show() throws IOException {
        phat.update();
    }

    /**
     * Set a specific pixel on or off. This works in the most efficient
     * way.
     *
     * @param x   The x coordinate.
     * @param y   The y coordinate.
     * @param pwm The PWM value.
     */
    @Override
    public void setPixel(int x, int y, int pwm) {
        if (x < 0 || x > MAX_X || y < 0 || y > MAX_Y)
            throw new IllegalArgumentException("Invalid co-ordinates for set");

        if (flip_x)
            x = MAX_X - x;

        if (flip_y)
            y = MAX_Y - y;

        // Piratical wiring madness!

        if (x >= 8) {
            x = (x - 8) * 2;
            y = MAX_Y - y;
        } else {
            x = 15 - x * 2;
        }

        phat.setLed(0, x * 8 + y, pwm);
    }

    /**
     * Set a pixel in the generic way.
     *
     * @param p     The pixel.
     * @param value The value.
     */
    @Override
    public void setPixel(Point p, Integer value) {
        setPixel(p.getX(), p.getY(), value.intValue());
    }

    /**
     * Optionally flip the x, y or both arguments. Useful for rotating
     * and other general diddling of the display,
     *
     * @param x Flip the x coordinates?
     * @param y Flip the y coordinates?
     */
    public void flip(boolean x, boolean y) {
        flip_x = x;
        flip_y = y;
    }

    /**
     * Get the maximum values for X and Y.
     *
     * @return A Point containing the maximum values.
     */
    @Override
    public Point getMax() {
        return MAX;
    }

    public static void main(String args[]) throws IOException, I2CFactory.UnsupportedBusNumberException, InterruptedException {
        ScrollPHATHD p = new ScrollPHATHD();

        MonoMatrixDemo.run(p);
    }


    public void draw_char(x, y, char, font =None, brightness =1.0, monospaced =false) {

        "Draw a single character to the buffer.;
        Returns the x & y coordinates of the bottom left - most corner of the drawn character.;
        param o_x Offset x -distance of the char from the left of the buffer;
        param o_y Offset y -distance of the char from the top of the buffer;
        param char Char to display -either an integer ordinal or a single letter;
        param font Font to use,public voidault is to use one specified with `set_font`
        param brightness Brightness of the pixels that compromise the char,from 0.0 to 1.0;
        param monospaced Whether to space characters out evenly;
        ""
        ";
        if (font is None){
            if (this._font is !None){
                font = this._font;
            } else;
            {
                return (x,y);
            }
        }
        if ( char in font.data){
            char_map = font.data[char];
        }
        if (type(char)is !int &ord(char)in font.data){
            char_map = font.data[ord(char)];
        } else;
        {
            return (x,y);
        }
        for px in range(len(char_map[0]));
        {
            for py in range(len(char_map));
            {
                pixel = char_map[py][px];
                if (pixel > 0) {
                    this.set_pixel(x + px, y + py, (pixel / 255.0) * brightness);
                }
            }
        }
        if (monospaced) {
            px = font.width - 1;
        }
        return (x + px,y + font.height);
    }
}
