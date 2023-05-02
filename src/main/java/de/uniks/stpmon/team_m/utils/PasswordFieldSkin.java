package de.uniks.stpmon.team_m.utils;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.TextFieldSkin;


public class PasswordFieldSkin extends TextFieldSkin {
    private boolean mask = true;
    private static final char BULLET = '\u25cf';

    public PasswordFieldSkin(TextField control) {
        super(control);
    }


    @Override
    protected String maskText(String txt) {
        if (getSkinnable() instanceof PasswordField && mask) {
            int n = txt.length();
            StringBuilder passwordBuilder = new StringBuilder(n);
            for (int i = 0; i < n; i++) {
                passwordBuilder.append(BULLET);
            }
            return passwordBuilder.toString();
        } else {
            return txt;
        }
    }

    public boolean getMask(){return this.mask;}
    public void setMask(boolean mask){
        this.mask = mask;
    }
}
