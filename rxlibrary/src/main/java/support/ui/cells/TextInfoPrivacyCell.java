package support.ui.cells;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import support.ui.utilities.AndroidUtilities;
import support.ui.utilities.LayoutHelper;
import support.ui.utilities.LocaleController;

public class TextInfoPrivacyCell extends FrameLayout {

  private TextView textView;

  public TextInfoPrivacyCell(Context context) {
    super(context);

    textView = new TextView(context);
    textView.setTextColor(CellUtils.getInfoColor(context));
    textView.setLinkTextColor(CellUtils.getLinkTextColor(context));
    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
    textView.setGravity(LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT);
    textView.setPadding(0, AndroidUtilities.dp(10), 0, AndroidUtilities.dp(17));
    textView.setMovementMethod(LinkMovementMethod.getInstance());
    addView(textView, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, (LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT) | Gravity.TOP, 17, 0, 17, 0));
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
  }

  public void setText(CharSequence text) {
    textView.setText(text);
  }

  public void setTextColor(int color) {
    textView.setTextColor(color);
  }
}
