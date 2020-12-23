package com.androiddreams.muzik.utilities;

import androidx.palette.graphics.Palette;

public class ColorPaletteGenerator {
    public int getBackgroundColorFromPalette(Palette palette) {
        if (palette != null) {
            int background = palette.getDarkVibrantColor(0xFF616261);
            if (background == 0xFF616261) background = palette.getDarkMutedColor(0xFF616261);
            if (background == 0xFF616261) background = palette.getVibrantColor(0xFF616261);
            if (background == 0xFF616261) background = palette.getMutedColor(0xFF616261);

            return background;
        }
        return 0xFF616261;
    }
}
