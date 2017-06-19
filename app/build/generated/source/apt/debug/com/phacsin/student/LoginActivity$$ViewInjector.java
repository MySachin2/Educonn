// Generated code from Butter Knife. Do not modify!
package com.phacsin.student;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class LoginActivity$$ViewInjector<T extends com.phacsin.student.LoginActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131493084, "field '_emailText'");
    target._emailText = finder.castView(view, 2131493084, "field '_emailText'");
    view = finder.findRequiredView(source, 2131493095, "field '_passwordText'");
    target._passwordText = finder.castView(view, 2131493095, "field '_passwordText'");
    view = finder.findRequiredView(source, 2131493096, "field '_loginButton'");
    target._loginButton = finder.castView(view, 2131493096, "field '_loginButton'");
  }

  @Override public void reset(T target) {
    target._emailText = null;
    target._passwordText = null;
    target._loginButton = null;
  }
}
