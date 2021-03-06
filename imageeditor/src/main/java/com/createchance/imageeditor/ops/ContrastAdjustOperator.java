package com.createchance.imageeditor.ops;

import android.opengl.GLES20;

import com.createchance.imageeditor.drawers.ContrastAdjustDrawer;

/**
 * Contrast adjust operator.
 *
 * @author createchance
 * @date 2018/11/11
 */
public class ContrastAdjustOperator extends AbstractOperator {
    private static final String TAG = "ContrastAdjustOperator";

    private final float MAX_CONTRAST = 2.0f;
    private final float MIN_CONTRAST = 0.0f;

    private float mContrast = 1.0f;

    private ContrastAdjustDrawer mDrawer;

    private ContrastAdjustOperator() {
        super(ContrastAdjustOperator.class.getSimpleName(), OP_CONTRAST_ADJUST);
    }

    @Override
    public boolean checkRational() {
        return mContrast >= MIN_CONTRAST && mContrast <= MAX_CONTRAST;
    }

    @Override
    public void exec() {
        mWorker.bindOffScreenFrameBuffer(mWorker.getTextures()[mWorker.getOutputTextureIndex()]);
        if (mDrawer == null) {
            mDrawer = new ContrastAdjustDrawer();
        }
        mDrawer.setContrast(mContrast);
        GLES20.glEnable(GLES20.GL_SCISSOR_TEST);
        GLES20.glScissor(mWorker.getImgShowLeft(),
                mWorker.getImgShowBottom(),
                mWorker.getImgShowWidth(),
                mWorker.getImgShowHeight());
        mDrawer.draw(mWorker.getTextures()[mWorker.getInputTextureIndex()],
                0,
                0,
                mWorker.getSurfaceWidth(),
                mWorker.getSurfaceHeight());
        GLES20.glDisable(GLES20.GL_SCISSOR_TEST);
        mWorker.bindDefaultFrameBuffer();
        mWorker.swapTexture();
    }

    public void setContrast(float contrast) {
        mContrast = contrast;
    }

    public float getContrast() {
        return mContrast;
    }

    public static class Builder {
        private ContrastAdjustOperator operator = new ContrastAdjustOperator();

        public Builder contrast(float contrast) {
            operator.mContrast = contrast;

            return this;
        }

        public ContrastAdjustOperator build() {
            return operator;
        }
    }
}
