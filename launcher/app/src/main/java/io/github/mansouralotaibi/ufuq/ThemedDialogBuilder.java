package io.github.mansouralotaibi.ufuq;
final class ThemedDialogBuilder extends android.app.AlertDialog.Builder {
 ThemedDialogBuilder(android.content.Context c) { super(c); }
 @Override public android.app.AlertDialog show() {
  android.app.AlertDialog d = super.show();
  UiTheme.apply(d.getWindow().getDecorView()); return d;
 }
}
