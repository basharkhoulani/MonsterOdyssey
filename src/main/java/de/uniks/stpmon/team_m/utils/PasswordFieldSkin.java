package de.uniks.stpmon.team_m.utils;

import de.uniks.stpmon.team_m.Constants;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.TextFieldSkin;


public class PasswordFieldSkin extends TextFieldSkin {
    private boolean mask = true;

    public PasswordFieldSkin(TextField control) {
        super(control);
    }


    @Override
    protected String maskText(String txt) {
        if (getSkinnable() instanceof PasswordField && mask) {
            int n = txt.length();
            return String.valueOf(Constants.BULLET).repeat(n);
        } else {
            return txt;
        }
    }

    public boolean getNotMask() {
        return !this.mask;
    }

    public void setMask(boolean mask) {
        this.mask = mask;
    }
}
