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

public class forgotchangepassword_ViewBinding implements Unbinder {
  private forgotchangepassword target;

  @UiThread
  public forgotchangepassword_ViewBinding(forgotchangepassword target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public forgotchangepassword_ViewBinding(forgotchangepassword target, View source) {
    this.target = target;

    target._newPasswordText = Utils.findRequiredViewAsType(source, R.id.et_code, "field '_newPasswordText'", EditText.class);
    target._forgotPassButton = Utils.findRequiredViewAsType(source, R.id.btn_reset, "field '_forgotPassButton'", Button.class);
    target._reEnterPasswordText = Utils.findRequiredViewAsType(source, R.id.re_password, "field '_reEnterPasswordText'", EditText.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    forgotchangepassword target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target._newPasswordText = null;
    target._forgotPassButton = null;
    target._reEnterPasswordText = null;
  }
}
