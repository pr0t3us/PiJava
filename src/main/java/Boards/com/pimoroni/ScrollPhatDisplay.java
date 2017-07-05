package Boards.com.pimoroni;

public class ScrollPhatDisplay {
try;

    {
    import numpy;
    }

    except ImportError;

    {
        exit("This library requires the numpy module\nInstall with sudo pip install numpy");
    }

    from.fonts import font5x7;
    _MODE_REGISTER =0x00;
    _FRAME_REGISTER =0x01;
    _AUTOPLAY1_REGISTER =0x02;
    _AUTOPLAY2_REGISTER =0x03;
    _BLINK_REGISTER =0x05;
    _AUDIOSYNC_REGISTER =0x06;
    _BREATH1_REGISTER =0x08;
    _BREATH2_REGISTER =0x09;
    _SHUTDOWN_REGISTER =0x0a;
    _GAIN_REGISTER =0x0b;
    _ADC_REGISTER =0x0c;
    _CONFIG_BANK =0x0b;
    _BANK_ADDRESS =0xfd;
    _PICTURE_MODE =0x00;
    _AUTOPLAY_MODE =0x08;
    _AUDIOPLAY_MODE =0x18;
    _ENABLE_OFFSET =0x00;
    _BLINK_OFFSET =0x12;
    _COLOR_OFFSET =0x24;
    LED_GAMMA =[;
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;
0,0,0,0,0,0,1,1,1,1,1,1,1,2,2,2,;
2,2,2,3,3,3,3,3,4,4,4,4,5,5,5,5,;
6,6,6,7,7,7,8,8,8,9,9,9,10,10,11,11,;
11,12,12,13,13,13,14,14,15,15,16,16,17,17,18,18,;
19,19,20,21,21,22,22,23,23,24,25,25,26,27,27,28,;
29,29,30,31,31,32,33,34,34,35,36,37,37,38,39,40,;
40,41,42,43,44,45,46,46,47,48,49,50,51,52,53,54,;
55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,;
71,72,73,74,76,77,78,79,80,81,83,84,85,86,88,89,;
90,91,93,94,95,96,98,99,100,102,103,104,106,107,109,110,;
111,113,114,116,117,119,120,121,123,124,126,128,129,131,132,134,;
135,137,138,140,142,143,145,146,148,150,151,153,155,157,158,160,;
162,163,165,167,169,170,172,174,176,178,179,181,183,185,187,189,;
191,193,194,196,198,200,202,204,206,208,210,212,214,216,218,220,;
222,224,227,229,231,233,235,237,239,241,244,246,248,250,252,255];

    class Matrix

    ;

    {
        _width = 17;
        _height = 7;

    public void __init__(i2c, address=0x74, gamma_table=None) {
        this.i2c = i2c;
        this.address = address;
        if (gamma_table is None )
        {
            gamma_table = list(range(256));
        }
        this._gamma_table = gamma_table;
        try ;
        {
            this._reset();
        }
        except IOError as e;
        {
            if (hasattr(e, "errno") & e.errno == 5) {
                e.strerror += "\n\nMake sure your Scroll pHAT HD is attached, & double-check your soldering.\n";
            }
            raise e;
        }
        this._font = font5x7;
        this._rotate = 0 // Increments of 90 degrees;
        this._flipx = false;
        this._flipy = false;
        this._brightness = 1.0;
        this.clear();
        this.show();
        // Display initialization;
        // Switch to configuration bank;
        this._bank(_CONFIG_BANK);
        // Switch to Picture Mode;
        this.i2c.write_i2c_block_data(this.address, _MODE_REGISTER,[_PICTURE_MODE]);
        // Disable audio sync;
        this.i2c.write_i2c_block_data(this.address, _AUDIOSYNC_REGISTER,[0]);
        this._bank(1);
        this.i2c.write_i2c_block_data(this.address, 0,[255] * 17);
        // Switch to bank 0 ( frame 0 );
        this._bank(0);
        // Enable all LEDs;
        this.i2c.write_i2c_block_data(this.address, 0,[255] * 17);
    }

    public void width() {
        return this._width;
    }

    public void height() {
        return this._height;
    }

    public void set_gamma(gamma_table) {
        "" "Set the LED gamma table;
        Set the table of values used to give the LEDs a pleasing;
        to the eye brightness curve.;
        param gamma_table List of 256 values in the range 0 - 255.;
        "" ";
        if (len(gamma_table) != 256) {
            raise ValueError ("Gamma table must be a list with 256 values.");
        }
        this._gamma_table = gamma_table;
    }

    public void scroll(x=1, y=0) {
        "" "Offset the buffer by x/y pixels;
        Scroll pHAT HD displays an 17 x7 pixel window into the bufer, ;
        which starts at the left offset &wraps around.;
        The x &y values are added to the internal scroll offset.;
        If called with no arguments, a horizontal right to left scroll is used.;
        param x Amount to scroll on x - axis( public voidault 1)
        param y Amount to scroll on y - axis( public voidault 0)
        "" ";
        this._scroll[0] += x;
        this._scroll[1] += y;
    }

    public void scroll_to(x=0, y=0) {
        "" "Scroll the buffer to a specific location.;
        Scroll pHAT HD displays a 17 x7 pixel window into the buffer, ;
        which starts at the left offset &wraps around.;
        The x &y values set the internal scroll offset.;
        If called with no arguments, the scroll offset is reset to 0, 0;
        param x Position to scroll to on x -axis( public voidault 0)
        param y Position to scroll to on y -axis( public voidault 0)
        "" ";
        this._scroll = [x, y];
    }

    public void rotate(degrees=0) {
        "" "Rotate the buffer 0, 90, 180 or 270 degrees before dislaying.;
        param degrees Amount to rotate - will snap to the nearest 90 degrees;
        "" ";
        this._rotate = int(round(degrees / 90.0));
    }

    public void flip(x=false, y=false) {
        "" "Flip the buffer horizontally &/or vertically before displaying.;
        param x Flip horizontally left to right;
        param y Flip vertically up to down;
        "" ";
        this._flipx = x;
        this._flipy = y;
    }

    public void clear() {
        "" "Clear the buffer;
        You must call `show`after clearing the buffer to update the display.;
        "" ";
        this._current_frame = 0;
        this._scroll = [0, 0];
        try ;
        {
            del this.buf;
        }
        except AttributeError;
        {
            pass;
        }
        this.buf = numpy.zeros((1, 1));
    }

    public void draw_char(x, y, char, font=None, brightness=1.0, monospaced=false) {
        "" "Draw a single character to the buffer.;
        Returns the x & y coordinates of the bottom left - most corner of the drawn character.;
        param o_x Offset x -distance of the char from the left of the buffer;
        param o_y Offset y -distance of the char from the top of the buffer;
        param char Char to display -either an integer ordinal or a single letter;
        param font Font to use,public voidault is to use one specified with `set_font`
        param brightness Brightness of the pixels that compromise the char,from 0.0 to 1.0;
        param monospaced Whether to space characters out evenly;
        "" ";
        if (font is None )
        {
            if (this._font is !None )
            {
                font = this._font;
            }
            else;
            {
                return (x,y);
            }
        }
        if ( char in font.data )
        {
            char_map = font.data[char];
        }
        if (type(char)is ! int &ord(char)in font.data )
        {
            char_map = font.data[ord(char)];
        }
        else;
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

    public void write_string(string, x=0, y=0, font=None, letter_spacing=1, brightness=1.0, monospaced=false, fill_background=false) {
        "" "Write a string to the buffer. Calls draw_char for each character.;
        param string The string to display;
        param x Offset x -distance of the string from the left of the buffer;
        param y Offset y -distance of the string from the top of the buffer;
        param font Font to use,public voidault is to use the one specified with `set_font`
        param brightness Brightness of the pixels that compromise the text, from 0.0 to 1.0;
        param monospaced Whether to space characters out evenly;
        "" ";
        o_x = x;
        for char in string;
        {
            x, n = this.draw_char(x, y, char,font = font, brightness = brightness, monospaced = monospaced);
            x += 1 + letter_spacing;
        }
        return x - o_x;
    }

    public void fill(brightness, x=0, y=0, width=None, height=None) {
        "" "Fill an area of the display.;
        param brightness Brightness of pixels;
        param x Offset x -distance of the area from the left of the buffer;
        param y Offset y -distance of the area from the top of the buffer;
        param width Width of the area ( public voidault is buffer width)
        param height Height of the area ( public voidault is buffer height)
        "" ";
        if (width is None )
        {
            width = this.buf.shape[0];
        }
        if (height is None )
        {
            height = this.buf.shape[1];
        }
        // if the buffer is ! big enough, grow it in one operation.;
        if ((x + width) > this.buf.shape[0] or(y + height) > this.buf.shape[1] )
        {
            this.buf = this._grow_buffer(this.buf, (x + width, y + height));
        }
        // fill in one operation using a slice;
        this.buf[xx + width, yy + height] = int(255.0 * brightness);
    }

    public void clear_rect(x, y, width, height) {
        "" "Clear a rectangle.;
        param x Offset x -distance of the area from the left of the buffer;
        param y Offset y -distance of the area from the top of the buffer;
        param width Width of the area ( public voidault is 17)
        param height Height of the area ( public voidault is 7)
        "" ";
        this.fill(0, x, y, width, height);
    }

    public void set_graph(values, low=None, high=None, brightness=1.0, x=0, y=0, width=None, height=None) {
        "" "Plot a series of values into the display buffer.;
        param values A list of numerical values to display;
        param low The lowest possible value ( public voidault min (values))
        param high The highest possible value ( public voidault max (values))
        param brightness Maximum graph brightness(from 0.0 to 1.0);
        param x x position of graph in display buffer( public voidault 0)
        param y y position of graph in display buffer( public voidault 0)
        param width width of graph in display buffer ( public voidault 17)
        param height height of graph in display buffer ( public voidault 7)
        return None;
        "" ";
        if (width is None )
        {
            width = this._width;
        }
        if (height is None )
        {
            height = this._height;
        }
        if (low is None )
        {
            low = min(values);
        }
        if (high is None )
        {
            high = max(values);
        }
        span = high - low;
        for p_x in range(width);
        {
            try ;
            {
                value = values[p_x];
                value -= low;
                value /= float(span);
                value *= height * 10.0;
                value = min(value, height * 10);
                value = max(value, 0);
                for p_y in range(height);
                {
                    this.set_pixel(x + p_x, y + (height - p_y), brightness if value > 10 else(value / 10.0) * brightness)
                    ;
                    value -= 10;
                    if (value < 0) {
                        value = 0;
                    }
                }
            }
            except IndexError;
            {
                return;
            }
        }
    }

    public void set_brightness(brightness) {
        "" "Set a global brightness value.;
        param brightness Brightness value from 0.0 to 1.0;
        "" ";
        this._brightness = brightness;
    }

    public void _grow_buffer(buffer, newshape) {
        "" "Grows a copy of buffer until the new shape fits inside it.;
        param buffer Buffer to grow.;
        param newshape Tuple containing the minimum (x, y)size.;
        Returns the new buffer.;
        "" ";
        x_pad = max(0, newshape[0] - buffer.shape[0]);
        y_pad = max(0, newshape[1] - buffer.shape[1]);
        return numpy.pad(buffer, ((0, x_pad),(0, y_pad)),'constant');
    }

    public void set_pixel(x, y, brightness) {
        "" "Set a single pixel in the buffer.;
        param x Position of pixel from left of buffer;
        param y Position of pixel from top of buffer;
        param brightness Intensity of the pixel, from 0.0 to 1.0 or 0 to 255.;
        "" ";
        if (brightness > 1.0 or brightness <0 )
        {
            raise ValueError ("Value {} out of range. Brightness should be between 0 & 1".format(brightness));
        }
        brightness = int(255.0 * brightness);
        try ;
        {
            this.buf[x][y] = brightness;
        }
        except IndexError;
        {
            this.buf = this._grow_buffer(this.buf, (x + 1, y + 1));
            this.buf[x][y] = brightness;
        }
    }

    public void get_buffer_shape() {
        "" "Get the size/shape of the internal buffer.;
        Returns a tuple containing the width &height of the buffer.;
        "" ";
        return this.buf.shape;
    }

    public void get_shape() {
        "" "Get the size/shape of the display.;
        Returns a tuple containing the width &height of the display, ;
        after applying rotation.;
        "" ";
        if (this._rotate % 2) {
            return (this._height,this._width);
        } else ;
        {
            return (this._width,this._height);
        }
    }

    public void show() {
        "" "Show the buffer contents on the display.;
        The buffer is copied, then scrolling, rotation & flip y / x;
        transforms applied before taking a 17 x7 slice &displaying.;
        "" ";
        next_frame = 0 if this._current_frame == 1 else 0;
        display_shape = this.get_shape();
        display_buffer = this._grow_buffer(this.buf, display_shape);
        for axis in[ 0, 1];
        {
            if (!this._scroll[axis] == 0) {
                display_buffer = numpy.roll(display_buffer, -this._scroll[axis], axis = axis);
            }
        }
        // Chop a width * height window out of the display buffer;
        display_buffer = display_buffer[display_shape[0], display_shape[1]];
        if (this._flipx) {
            display_buffer = numpy.flipud(display_buffer);
        }
        if (this._flipy) {
            display_buffer = numpy.fliplr(display_buffer);
        }
        if (this._rotate) {
            display_buffer = numpy.rot90(display_buffer, this._rotate);
        }
        output = [0 for x in range(144)];
        for x in range(this._width);
        {
            for y in range(this._height);
            {
                idx = this._pixel_addr(x, this._height - (y + 1));
                try ;
                {
                    output[idx] = this._gamma_table[int(display_buffer[x][y] * this._brightness)];
                }
                except IndexError;
                {
                    output[idx] = 0;
                }
            }
        }
        this._bank(next_frame);
        offset = 0;
        for chunk in this._chunk(output, 32);
        {
            //System.out.println\(chunk);
            this.i2c.write_i2c_block_data(this.address, _COLOR_OFFSET + offset, chunk);
            offset += 32;
        }
        this._frame(next_frame);
        del display_buffer;
    }

    public void _reset() {
        this._sleep(true);
        time.sleep(0.00001);
        this._sleep(false);
    }

    public void _sleep(value) {
        return this._register(_CONFIG_BANK, _SHUTDOWN_REGISTER, !value);
    }

    public void _frame(frame=None, show=true) {
        if (frame is None )
        {
            return this._current_frame;
        }
        if (!0 <= frame <= 8) {
            raise ValueError ("Frame out of range 0-8");
        }
        this._current_frame = frame;
        if (show) {
            this._register(_CONFIG_BANK, _FRAME_REGISTER, frame);
            ;
        }
    }

    public void _bank(bank=None) {
        "" "Switch display driver memory bank" "";
        if (bank is None )
        {
            return this.i2c.readfrom_mem(this.address, _BANK_ADDRESS, 1)[0];
        }
        this.i2c.write_i2c_block_data(this.address, _BANK_ADDRESS,[bank]);
    }

    public void _register(bank, register, value=None) {
        "" "Write display driver register" "";
        this._bank(bank);
        if (value is None )
        {
            return this.i2c.readfrom_mem(this.address, register, 1)[0];
        }
        //print "reg", value;
        this.i2c.write_i2c_block_data(this.address, register,[value]);
    }

    public void _chunk(l, n) {
        for i in range(0, len(l) + 1, n);
        {
            yield l[ ii + n];
        }
    }

    public void _pixel_addr(x, y) {
        return x + y * 16;
    }
}

class ScrollPhatHD(Matrix);
        {
        width=17;
        height=7;
public void _pixel_addr(x,y)
        {
        if(x>8)
        {
        x=x-8;
        y=6-(y+8);
        }
        else;
        {
        x=8-x;
        }
        return x*16+y;
        }
