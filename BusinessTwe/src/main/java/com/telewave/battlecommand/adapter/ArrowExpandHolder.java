package com.telewave.battlecommand.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.telewave.business.twe.R;
import com.unnamed.b.atv.model.TreeNode;


public class ArrowExpandHolder extends TreeNode.BaseNodeViewHolder<ArrowExpandHolder.IconTreeItem> {
    private TextView tvValue;
    private ImageView arrowView;
    private ImageView icMemberIcon;

    public ArrowExpandHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node, IconTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_selectable_header, null, false);

        tvValue = (TextView) view.findViewById(R.id.node_value);
        tvValue.setText(value.text);

        arrowView = (ImageView) view.findViewById(R.id.arrow_icon);
        icMemberIcon = (ImageView) view.findViewById(R.id.ic_member_icon);
        arrowView.setPadding(20, 10, 10, 10);
        if (value.type.equals("3")) {
            arrowView.setVisibility(View.GONE);
            icMemberIcon.setVisibility(View.VISIBLE);
        }
        arrowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tView.toggleNode(node);
            }
        });

        return view;
    }

    @Override
    public void toggle(boolean active) {
        if (active) {
            arrowView.setBackgroundResource(R.mipmap.down_arrow);
        } else {
            arrowView.setBackgroundResource(R.mipmap.right_arrow);
        }
    }

    @Override
    public void toggleSelectionMode(boolean editModeEnabled) {
    }

    public static class IconTreeItem {
        public String id;
        public String text;
        public String type;

        public IconTreeItem(String id, String text, String type) {
            this.id = id;
            this.text = text;
            this.type = type;
        }
    }

}
