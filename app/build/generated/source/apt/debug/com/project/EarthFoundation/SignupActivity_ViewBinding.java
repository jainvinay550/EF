// Generated code from Butter Knife. Do not modify!
package com.project.EarthFoundation;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SignupActivity_ViewBinding implements Unbinder {
  private SignupActivity target;

  @UiThread
  public SignupActivity_ViewBinding(SignupActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SignupActivity_ViewBinding(SignupActivity target, View source) {
    this.target = target;

    target._fnameText = Utils.findRequiredViewAsType(source, R.id.input_fname, "field '_fnameText'", EditText.class);
    target._lnameText = Utils.findRequiredViewAsType(source, R.id.input_lname, "field '_lnameText'", EditText.class);
    target._addressText = Utils.findRequiredViewAsType(source, R.id.input_address, "field '_addressText'", EditText.class);
    target._cityText = Utils.findRequiredViewAsType(source, R.id.input_city, "field '_cityText'", EditText.class);
    target._pinText = Utils.findRequiredViewAsType(source, R.id.input_pin, "field '_pinText'", EditText.class);
    target._aadharText = Utils.findRequiredViewAsType(source, R.id.input_aadhar, "field '_aadharText'", EditText.class);
    target._emailText = Utils.findRequiredViewAsType(source, R.id.input_email, "field '_emailText'", EditText.class);
    target._mobileText = Utils.findRequiredViewAsType(source, R.id.input_mobile, "field '_mobileText'", EditText.class);
    target._passwordText = Utils.findRequiredViewAsType(source, R.id.input_password, "field '_passwordText'", EditText.class);
    target._reEnterPasswordText = Utils.findRequiredViewAsType(source, R.id.input_reEnterPassword, "field '_reEnterPasswordText'", EditText.class);
    target._signupButton = Utils.findRequiredViewAsType(source, R.id.btn_signup, "field '_signupButton'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SignupActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target._fnameText = null;
    target._lnameText = null;
    target._addressText = null;
    target._cityText = null;
    target._pinText = null;
    target._aadharText = null;
    target._emailText = null;
    target._mobileText = null;
    target._passwordText = null;
    target._reEnterPasswordText = null;
    target._signupButton = null;
  }
}
