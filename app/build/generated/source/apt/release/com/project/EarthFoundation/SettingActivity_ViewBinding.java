// Generated code from Butter Knife. Do not modify!
package com.project.EarthFoundation;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SettingActivity_ViewBinding implements Unbinder {
  private SettingActivity target;

  @UiThread
  public SettingActivity_ViewBinding(SettingActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SettingActivity_ViewBinding(SettingActivity target, View source) {
    this.target = target;

    target._oldPasswordText = Utils.findRequiredViewAsType(source, R.id.old_password, "field '_oldPasswordText'", EditText.class);
    target._newPasswordText = Utils.findRequiredViewAsType(source, R.id.et_code, "field '_newPasswordText'", EditText.class);
    target._reEnterPasswordText = Utils.findRequiredViewAsType(source, R.id.re_password, "field '_reEnterPasswordText'", EditText.class);
    target._changePassButton = Utils.findRequiredViewAsType(source, R.id.btn_changepassword, "field '_changePassButton'", Button.class);
    target.coordinatorLayout = Utils.findRequiredViewAsType(source, R.id.setting_layout, "field 'coordinatorLayout'", CoordinatorLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SettingActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target._oldPasswordText = null;
    target._newPasswordText = null;
    target._reEnterPasswordText = null;
    target._changePassButton = null;
    target.coordinatorLayout = null;
  }
}
