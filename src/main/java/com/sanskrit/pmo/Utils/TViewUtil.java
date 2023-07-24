package com.sanskrit.pmo.Utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TViewUtil {

    public static final class EmptyViewBuilder {
        private String actionText;
        private String emptyText;
        private int emptyTextColor = -1;
        private int emptyTextSize;
        private Drawable iconDrawable;
        private int iconSrc;
        private LayoutParams layoutParams;
        private OnClickListener mAction;
        private Context mContext;
        private ShowType mShowButton = ShowType.DEFAULT;
        private ShowType mShowIcon = ShowType.DEFAULT;
        private ShowType mShowText = ShowType.DEFAULT;

        private enum ShowType {
            DEFAULT,
            SHOW,
            HIDE
        }

        public static EmptyViewBuilder getInstance(Context context) {
            return new EmptyViewBuilder(context);
        }

        public void bindView(AdapterView listView) {
            TEmptyView emptyView = TViewUtil.genSimpleEmptyView(listView);
            TViewUtil.removeExistEmptyView(listView);
            listView.setEmptyView(emptyView);
            setEmptyViewStyle(emptyView);
        }

        public void bindView(final RecyclerView recyclerView) {
            final TEmptyView emptyView = TViewUtil.genSimpleEmptyView(recyclerView);
            final RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if (adapter != null) {
                RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
                    public void onChanged() {
                        super.onChanged();
                        if (adapter.getItemCount() > 0) {
                            recyclerView.setVisibility(0);
                            emptyView.setVisibility(8);
                            return;
                        }
                        recyclerView.setVisibility(8);
                        emptyView.setVisibility(0);
                    }
                };
                adapter.registerAdapterDataObserver(observer);
                observer.onChanged();
                setEmptyViewStyle(emptyView);
                return;
            }
            throw new RuntimeException("This RecyclerView has no adapter, you must call setAdapter first!");
        }

        private void setEmptyViewStyle(TEmptyView emptyView) {
            boolean canShowText;
            boolean canShowIcon;
            boolean canShowButton;
            if (this.mShowText == ShowType.SHOW || (this.mShowText == ShowType.DEFAULT && TEmptyView.hasDefaultConfig() && TEmptyView.getConfig().mShowText == ShowType.SHOW)) {
                canShowText = true;
            } else {
                canShowText = false;
            }
            emptyView.setShowText(canShowText);
            if (canShowText) {
                if (this.emptyTextColor != -1) {
                    emptyView.setTextColor(this.emptyTextColor);
                } else if (TEmptyView.hasDefaultConfig() && TEmptyView.getConfig().emptyTextColor != -1) {
                    emptyView.setTextColor(TEmptyView.getConfig().emptyTextColor);
                }
                if (this.emptyTextSize != 0) {
                    emptyView.setTextSize((float) this.emptyTextSize);
                } else if (TEmptyView.hasDefaultConfig() && TEmptyView.getConfig().emptyTextSize != 0) {
                    emptyView.setTextSize((float) TEmptyView.getConfig().emptyTextSize);
                }
                if (!TextUtils.isEmpty(this.emptyText)) {
                    emptyView.setEmptyText(this.emptyText);
                } else if (TEmptyView.hasDefaultConfig() && !TextUtils.isEmpty(TEmptyView.getConfig().emptyText)) {
                    emptyView.setEmptyText(TEmptyView.getConfig().emptyText);
                }
            }
            if (this.mShowIcon == ShowType.SHOW || (this.mShowIcon == ShowType.DEFAULT && TEmptyView.hasDefaultConfig() && TEmptyView.getConfig().mShowIcon == ShowType.SHOW)) {
                canShowIcon = true;
            } else {
                canShowIcon = false;
            }
            emptyView.setShowIcon(canShowIcon);
            if (canShowIcon) {
                if (this.iconSrc != 0) {
                    emptyView.setIcon(this.iconSrc);
                } else if (TEmptyView.hasDefaultConfig() && TEmptyView.getConfig().iconSrc != 0) {
                    emptyView.setIcon(TEmptyView.getConfig().iconSrc);
                }
                if (this.iconDrawable != null) {
                    emptyView.setIcon(this.iconDrawable);
                } else if (TEmptyView.hasDefaultConfig() && TEmptyView.getConfig().iconDrawable != null) {
                    emptyView.setIcon(TEmptyView.getConfig().iconDrawable);
                }
            }
            if (this.mShowButton == ShowType.SHOW || (this.mShowButton == ShowType.DEFAULT && TEmptyView.hasDefaultConfig() && TEmptyView.getConfig().mShowButton == ShowType.SHOW)) {
                canShowButton = true;
            } else {
                canShowButton = false;
            }
            emptyView.setShowButton(canShowButton);
            if (canShowButton) {
                if (!TextUtils.isEmpty(this.actionText)) {
                    emptyView.setActionText(this.actionText);
                } else if (TEmptyView.hasDefaultConfig() && !TextUtils.isEmpty(TEmptyView.getConfig().actionText)) {
                    emptyView.setActionText(TEmptyView.getConfig().actionText);
                }
                if (this.mAction != null) {
                    emptyView.setAction(this.mAction);
                } else if (TEmptyView.hasDefaultConfig() && TEmptyView.getConfig().mAction != null) {
                    emptyView.setAction(TEmptyView.getConfig().mAction);
                }
            }
            if (this.layoutParams != null) {
                emptyView.setLayoutParams(this.layoutParams);
            } else if (TEmptyView.hasDefaultConfig() && TEmptyView.getConfig().layoutParams != null) {
                emptyView.setLayoutParams(TEmptyView.getConfig().layoutParams);
            }
        }

        private EmptyViewBuilder(Context context) {
            this.mContext = context;
        }

        public EmptyViewBuilder setEmptyText(String text) {
            this.emptyText = text;
            return this;
        }

        public EmptyViewBuilder setEmptyText(int textResID) {
            this.emptyText = this.mContext.getString(textResID);
            return this;
        }

        public EmptyViewBuilder setEmptyTextColor(int color) {
            this.emptyTextColor = color;
            return this;
        }

        public EmptyViewBuilder setEmptyTextSize(int textSize) {
            this.emptyTextSize = textSize;
            return this;
        }

        public EmptyViewBuilder setEmptyTextSizePX(int textSizePX) {
            this.emptyTextSize = TViewUtil.px2sp(this.mContext, (float) textSizePX);
            return this;
        }

        public EmptyViewBuilder setIconSrc(int iconSrc) {
            this.iconSrc = iconSrc;
            this.iconDrawable = null;
            return this;
        }

        public EmptyViewBuilder setIconDrawable(Drawable iconDrawable) {
            this.iconDrawable = iconDrawable;
            this.iconSrc = 0;
            return this;
        }

        public EmptyViewBuilder setLayoutParams(LayoutParams layoutParams) {
            this.layoutParams = layoutParams;
            return this;
        }

        public EmptyViewBuilder setShowIcon(boolean mShowIcon) {
            this.mShowIcon = mShowIcon ? ShowType.SHOW : ShowType.HIDE;
            return this;
        }

        public EmptyViewBuilder setShowText(boolean showText) {
            this.mShowText = showText ? ShowType.SHOW : ShowType.HIDE;
            return this;
        }

        public EmptyViewBuilder setShowButton(boolean showButton) {
            this.mShowButton = showButton ? ShowType.SHOW : ShowType.HIDE;
            return this;
        }

        public EmptyViewBuilder setAction(OnClickListener onClickListener) {
            this.mAction = onClickListener;
            return this;
        }

        public EmptyViewBuilder setActionText(String actionText) {
            this.actionText = actionText;
            return this;
        }
    }

    public static void setEmptyView(AdapterView listView) {
        if (TEmptyView.hasDefaultConfig()) {
            TEmptyView.getConfig().bindView(listView);
        } else {
            listView.setEmptyView(genSimpleEmptyView(listView));
        }
    }

    @NonNull
    private static TEmptyView genSimpleEmptyView(View view) {
        TEmptyView emptyView = new TEmptyView(view.getContext(), null);
        ViewParent parent = view.getParent().getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).addView(emptyView);
            if (parent instanceof LinearLayout) {
                Log.e("lol", "here");
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) emptyView.getLayoutParams();
                lp.height = -1;
                lp.gravity = 17;
                emptyView.setLayoutParams(lp);
            } else if (parent instanceof RelativeLayout) {
                RelativeLayout.LayoutParams lp2 = (RelativeLayout.LayoutParams) emptyView.getLayoutParams();
                lp2.addRule(13, -1);
                emptyView.setLayoutParams(lp2);
            } else if (parent instanceof FrameLayout) {
                FrameLayout.LayoutParams lp3 = (FrameLayout.LayoutParams) emptyView.getLayoutParams();
                lp3.height = -1;
                lp3.gravity = 17;
                emptyView.setLayoutParams(lp3);
            }
        }
        return emptyView;
    }

    public static void setEmptyView(AdapterView listView, EmptyViewBuilder builder) {
        builder.bindView(listView);
    }

    private static void removeExistEmptyView(AdapterView listView) {
        if (listView.getEmptyView() != null) {
            ViewParent rootView = listView.getParent();
            if (rootView instanceof ViewGroup) {
                ((ViewGroup) rootView).removeView(listView.getEmptyView());
            }
        }
    }

    public static int sp2px(Context context, float spValue) {
        return (int) TypedValue.applyDimension(2, spValue, context.getResources().getDisplayMetrics());
    }

    public static int px2sp(Context context, float pxValue) {
        return (int) ((pxValue / context.getResources().getDisplayMetrics().scaledDensity) + 0.5f);
    }
}
