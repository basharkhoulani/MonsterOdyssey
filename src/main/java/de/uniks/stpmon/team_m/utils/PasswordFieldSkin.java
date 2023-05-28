package de.uniks.stpmon.team_m.utils;

import de.uniks.stpmon.team_m.Constants;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.TextFieldSkin;


public class PasswordFieldSkin extends TextFieldSkin {
    private boolean mask = true;

    /**
     * PasswordFieldSkin handles the masking of the password field.
     *
     * @param control Password field to be masked.
     */

    public PasswordFieldSkin(TextField control) {
        super(control);
    }

    /**
     * This method masks the password field.
     *
     * @param txt Text to be masked.
     * @return Masked text.
     */

    @Override
    protected String maskText(String txt) {
        if (getSkinnable() instanceof PasswordField && mask) {
            int n = txt.length();
            return String.valueOf(Constants.BULLET).repeat(n);
        } else {
            return txt;
        }
    }

    /**
     * This method returns the mask value.
     *
     * @return Mask.
     */

    public boolean getNotMask() {
        return !this.mask;
    }

    /**
     * This method sets the mask value.
     *
     * @param mask Mask to be set.
     */

    public void setMask(boolean mask) {
        this.mask = mask;
    }
}
