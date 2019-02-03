// Generated code from Butter Knife. Do not modify!
package com.project.EarthFoundation;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class forgotpassword_ViewBinding implements Unbinder {
  private forgotpassword target;

  @UiThread
  public forgotpassword_ViewBinding(forgotpassword target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public forgotpassword_ViewBinding(forgotpassword target, View source) {
    this.target = target;

    target._emailText = Utils.findRequiredViewAsType(source, R.id.et_email, "field '_emailText'", EditText.class);
    target._forgotPassButton = Utils.findRequiredViewAsType(source, R.id.btn_reset, "field '_forgotPassButton'", Button.class);
    target._otp = Utils.findRequiredViewAsType(source, R.id.et_code, "field '_otp'", EditText.class);
    target._emailNotification = Utils.findRequiredViewAsType(source, R.id.emailNotification, "field '_emailNotification'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    forgotpassword target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target._emailText = null;
    target._forgotPassButton = null;
    target._otp = null;
    target._emailNotification = null;
  }
}
