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
 * License along with this library; if not, see
 * <http://www.gnu.org/licenses/>.
 */

package Graphics;

/**
 * This class flips the X coordinates of a point around. So if the input x
 * range is 0 to n the mapped X point will be n - x. The y coordinate remains
 * the same.
 * 
 * @author Jim Darby
 */
public class Identity extends Mapping
{
    /**
     * Create a mapping given the width and height of the input. Note that the
     * valid X coordinates are from 0 to width - 1 and the valid Y coordinates
     * are from 0 to height - 1.
     * 
     * @param width The input width.
     * @param height The input height.
     */
    public Identity (int width, int height)
    {
	super (new Point (width - 1, height - 1));
    }

    /**
     * Create a mapping given a previous mapping. The width and height are
     * inherited from the previous item.
     * 
     * @param before The previous mapping.
     */
    public Identity (Mapping before)
    {
	super (before, before.getOutMax ());
    }
    
    /**
     * Perform a mapping. This performs no actual action on the Point!
     * 
     * @param p The input point.
     * @return The mapped result.
     */
    @Override
    public Point map (Point p)
    {
	if (before != null)
	    p = before.map (p);
	
	validateIn (p);

	final Point result = p;

	validateOut (result);
	
	return result;
    }

    /**
     * Return a printable form of the mapping.
     * 
     * @return The String representation.
     */
    @Override
    public String toString ()
    {
	String result = "Identity from " + getInMax () + " to " + getOutMax ();

	if (before != null)
	    result = before.toString () + ' ' + result;
	
	return result;
    }
}

