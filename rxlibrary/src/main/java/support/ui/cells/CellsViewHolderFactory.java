package support.ui.cells;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.anthony.rxlibrary.R;

import support.ui.adapters.BaseEasyViewHolderFactory;
import support.ui.adapters.EasyViewHolder;

public class CellsViewHolderFactory extends BaseEasyViewHolderFactory {

  public CellsViewHolderFactory(Context context) {
    super(context);
  }

  @Override
  public EasyViewHolder create(int viewType, ViewGroup parent) {
    CellViewHolder cellViewHolder = null;
    switch (viewType) {
      case CellModel.VIEW_TYPE_LOADING: {
        cellViewHolder = new CellViewHolder(new LoadingCell(context));
        break;
      }
      case CellModel.VIEW_TYPE_DIVIDER: {
        cellViewHolder = new CellViewHolder(new DividerCell(context));
        break;
      }
      case CellModel.VIEW_TYPE_EMPTY: {
        cellViewHolder = new CellViewHolder(new EmptyCell(context)) {
          @Override
          public void bindTo(int position, CellModel model) {
            if (model.cellHeight > 0) {
              ((EmptyCell)itemView).setHeight(model.cellHeight);
            }
          }
        };
        break;
      }
      case CellModel.VIEW_TYPE_HEADER: {
        cellViewHolder = new CellViewHolder(new HeaderCell(context)) {
          @Override
          public void bindTo(int position, CellModel model) {
            if (!TextUtils.isEmpty(model.text)) {
              ((HeaderCell)itemView).setText(model.text);
            }
          }
        };
        break;
      }
      case CellModel.VIEW_TYPE_SHADOW: {
        cellViewHolder = new CellViewHolder(new ShadowSectionCell(context)) {
          @Override
          public void bindTo(int position, CellModel model) {
            if (model.cellHeight > 0) {
              ((ShadowSectionCell)itemView).setSize(model.cellHeight);
            }
          }
        };
        break;
      }
      case CellModel.VIEW_TYPE_SHADOW_BOTTOM: {
        cellViewHolder = new CellViewHolder(new ShadowBottomSectionCell(context));
        break;
      }
      case CellModel.VIEW_TYPE_TEXT:{
        cellViewHolder = new CellViewHolder(new TextCell(context)) {
          @Override
          public void bindTo(int position, CellModel model) {
            TextCell textCell = (TextCell) itemView;
            textCell.bindView(model);
          }
        };
        break;
      }
      case CellModel.VIEW_TYPE_TEXT_INFO_PRIVACY:{
        cellViewHolder = new CellViewHolder(new TextInfoPrivacyCell(context)) {
          @Override
          public void bindTo(int position, CellModel model) {
            TextInfoPrivacyCell textCell = (TextInfoPrivacyCell) itemView;
            textCell.setText(model.text);
            if (model.isBottom) {
              textCell.setBackgroundResource(R.drawable.greydivider_bottom);
            } else {
              textCell.setBackgroundResource(R.drawable.greydivider);
            }
          }
        };
        break;
      }
      case CellModel.VIEW_TYPE_SETTINGS:{
        cellViewHolder = new CellViewHolder(new TextSettingsCell(context)) {
          @Override
          public void bindTo(int position, CellModel model) {
            TextSettingsCell cell = (TextSettingsCell) itemView;
            cell.bindView(model.text, model.drawable, model.value, model.needDivider);
          }
        };
        break;
      }
      case CellModel.VIEW_TYPE_DETAIL_SETTINGS:{
        cellViewHolder = new CellViewHolder(new TextDetailSettingsCell(context)) {
          @Override
          public void bindTo(int position, CellModel model) {
            TextDetailSettingsCell cell = (TextDetailSettingsCell) itemView;
            cell.setTextAndValue(model.text, model.detail, model.needDivider);
            cell.setMultilineDetail(model.multiline);
          }
        };
        break;
      }
      case CellModel.VIEW_TYPE_CHECK:{
        cellViewHolder = new CellViewHolder(new TextCheckCell(context)) {
          @Override
          public void bindTo(int position, CellModel model) {
            TextCheckCell cell = (TextCheckCell) itemView;
            cell.bindView(model.text, model.detail, model.checked, model.needDivider);
          }
        };
        break;
      }
      default:
        return super.create(viewType, parent);
    }
    return cellViewHolder;
  }

  @Override
  public int itemViewType(Object object) {
    if (object instanceof CellModel) {
      return ((CellModel)object).itemViewType;
    }
    return super.itemViewType(object);
  }
}
