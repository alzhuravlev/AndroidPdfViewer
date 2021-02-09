package com.crane.pdfviewer.render.math;

import android.graphics.Matrix;
import android.graphics.RectF;

public class Mat4 {
    public float m00, m01, m02, m03;
    public float m10, m11, m12, m13;
    public float m20, m21, m22, m23;
    public float m30, m31, m32, m33;

    private static Mat4 tmp = new Mat4();
    private static Mat4 tmp_dot = new Mat4();
    private static float[] tmpF = new float[9];

    public Mat4() {
        identity();
    }

    public Mat4(float v) {
        set(v);
    }

    public Mat4(Mat4 src) {
        set(src);
    }

    public static Mat4 dot(Mat4 l, Mat4 r) {
        Mat4 m = new Mat4();
        dot(l, r, m);
        return m;
    }

    public static void dot(Mat4 l, Mat4 r, Mat4 res) {
//        public float m00, m01, m02, m03;
//        public float m10, m11, m12, m13;
//        public float m20, m21, m22, m23;
//        public float m30, m31, m32, m33;
        tmp_dot.m00 = l.m00 * r.m00 + l.m01 * r.m10 + l.m02 * r.m20 + l.m03 * r.m30;
        tmp_dot.m01 = l.m00 * r.m01 + l.m01 * r.m11 + l.m02 * r.m21 + l.m03 * r.m31;
        tmp_dot.m02 = l.m00 * r.m02 + l.m01 * r.m12 + l.m02 * r.m22 + l.m03 * r.m32;
        tmp_dot.m03 = l.m00 * r.m03 + l.m01 * r.m13 + l.m02 * r.m23 + l.m03 * r.m33;

        tmp_dot.m10 = l.m10 * r.m00 + l.m11 * r.m10 + l.m12 * r.m20 + l.m13 * r.m30;
        tmp_dot.m11 = l.m10 * r.m01 + l.m11 * r.m11 + l.m12 * r.m21 + l.m13 * r.m31;
        tmp_dot.m12 = l.m10 * r.m02 + l.m11 * r.m12 + l.m12 * r.m22 + l.m13 * r.m32;
        tmp_dot.m13 = l.m10 * r.m03 + l.m11 * r.m13 + l.m12 * r.m23 + l.m13 * r.m33;

        tmp_dot.m20 = l.m20 * r.m00 + l.m21 * r.m10 + l.m22 * r.m20 + l.m23 * r.m30;
        tmp_dot.m21 = l.m20 * r.m01 + l.m21 * r.m11 + l.m22 * r.m21 + l.m23 * r.m31;
        tmp_dot.m22 = l.m20 * r.m02 + l.m21 * r.m12 + l.m22 * r.m22 + l.m23 * r.m32;
        tmp_dot.m23 = l.m20 * r.m03 + l.m21 * r.m13 + l.m22 * r.m23 + l.m23 * r.m33;

        tmp_dot.m30 = l.m30 * r.m00 + l.m31 * r.m10 + l.m32 * r.m20 + l.m33 * r.m30;
        tmp_dot.m31 = l.m30 * r.m01 + l.m31 * r.m11 + l.m32 * r.m21 + l.m33 * r.m31;
        tmp_dot.m32 = l.m30 * r.m02 + l.m31 * r.m12 + l.m32 * r.m22 + l.m33 * r.m32;
        tmp_dot.m33 = l.m30 * r.m03 + l.m31 * r.m13 + l.m32 * r.m23 + l.m33 * r.m33;

        res.set(tmp_dot);
    }

    public Mat4 set(Mat4 src) {
        m00 = src.m00;
        m01 = src.m01;
        m02 = src.m02;
        m03 = src.m03;

        m10 = src.m10;
        m11 = src.m11;
        m12 = src.m12;
        m13 = src.m13;

        m20 = src.m20;
        m21 = src.m21;
        m22 = src.m22;
        m23 = src.m23;

        m30 = src.m30;
        m31 = src.m31;
        m32 = src.m32;
        m33 = src.m33;

        return this;
    }

    public Mat4 set(float m00, float m01, float m02, float m03,
                    float m10, float m11, float m12, float m13,
                    float m20, float m21, float m22, float m23,
                    float m30, float m31, float m32, float m33) {

        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m03 = m03;

        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
        this.m13 = m13;

        this.m20 = m20;
        this.m21 = m21;
        this.m22 = m22;
        this.m23 = m23;

        this.m30 = m30;
        this.m31 = m31;
        this.m32 = m32;
        this.m33 = m33;

        return this;
    }

    public Mat4 set(float v) {

        this.m00 = v;
        this.m01 = v;
        this.m02 = v;
        this.m03 = v;

        this.m10 = v;
        this.m11 = v;
        this.m12 = v;
        this.m13 = v;

        this.m20 = v;
        this.m21 = v;
        this.m22 = v;
        this.m23 = v;

        this.m30 = v;
        this.m31 = v;
        this.m32 = v;
        this.m33 = v;

        return this;
    }

    public Mat4 identity() {

        m00 = 1.0f;
        m01 = 0.0f;
        m02 = 0.0f;
        m03 = 0.0f;

        m10 = 0.0f;
        m11 = 1.0f;
        m12 = 0.0f;
        m13 = 0.0f;

        m20 = 0.0f;
        m21 = 0.0f;
        m22 = 1.0f;
        m23 = 0.0f;

        m30 = 0.0f;
        m31 = 0.0f;
        m32 = 0.0f;
        m33 = 1.0f;

        return this;
    }

    public static float sin(float a) {
        return (float) Math.sin(a);
    }

    public static float cos(float a) {
        return (float) Math.cos(a);
    }

    public static float tan(float a) {
        return (float) Math.tan(a);
    }

    public Mat4 translate(float x, float y, float z) {
        tmp.identity();
        tmp.m03 = x;
        tmp.m13 = y;
        tmp.m23 = z;
        dot(tmp, this, this);
        return this;
    }

    public Mat4 rotateDegX(float a) {
        rotateX((float) (a * Math.PI / 180.0));
        return this;
    }

    public Mat4 rotateDegY(float a) {
        rotateY((float) (a * Math.PI / 180.0));
        return this;
    }

    public Mat4 rotateDegZ(float a) {
        rotateZ((float) (a * Math.PI / 180.0));
        return this;
    }

    public Mat4 rotateX(float a) {
//        public float m00, m01, m02, m03;
//        public float m10, m11, m12, m13;
//        public float m20, m21, m22, m23;
//        public float m30, m31, m32, m33;
        tmp.identity();
        tmp.m11 = cos(a);
        tmp.m12 = -sin(a);
        tmp.m21 = sin(a);
        tmp.m22 = cos(a);
        dot(tmp, this, this);
        return this;
    }

    public Mat4 rotateY(float a) {
//        public float m00, m01, m02, m03;
//        public float m10, m11, m12, m13;
//        public float m20, m21, m22, m23;
//        public float m30, m31, m32, m33;
        tmp.identity();
        tmp.m00 = cos(a);
        tmp.m02 = sin(a);
        tmp.m20 = -sin(a);
        tmp.m22 = cos(a);
        dot(tmp, this, this);
        return this;
    }

    public Mat4 rotateAxisDeg(Vec4 v, float a) {
        rotateAxisDeg(v.x, v.y, v.z, a);
        return this;
    }

    public Mat4 rotateAxisDeg(float ax, float ay, float az, float a) {
        rotateAxis(ax, ay, az, (float) (a * Math.PI / 180.0));
        return this;
    }

    public Mat4 rotateQuaternion(float qx, float qy, float qz, float qw) {
        tmp.identity();
        tmp.fromQuaternion(qx, qy, qz, qw);
        dot(tmp, this, this);
        return this;
    }

    public Mat4 fromQuaternion(Vec4 q) {
        fromQuaternion(q.x, q.y, q.z, q.w);
        return this;
    }

    public Mat4 fromQuaternion(float qx, float qy, float qz, float qw) {
        m00 = 1.0f - 2.0f * qy * qy - 2.0f * qz * qz;
        m01 = 2.0f * qx * qy - 2.0f * qz * qw;
        m02 = 2.0f * qx * qz + 2.0f * qy * qw;
        m03 = 0.0f;

        m10 = 2.0f * qx * qy + 2.0f * qz * qw;
        m11 = 1.0f - 2.0f * qx * qx - 2.0f * qz * qz;
        m12 = 2.0f * qy * qz - 2.0f * qx * qw;
        m13 = 0.0f;

        m20 = 2.0f * qx * qz - 2.0f * qy * qw;
        m21 = 2.0f * qy * qz + 2.0f * qx * qw;
        m22 = 1.0f - 2.0f * qx * qx - 2.0f * qy * qy;
        m23 = 0.0f;

        m30 = 0.0f;
        m31 = 0.0f;
        m32 = 0.0f;
        m33 = 1.0f;
        return this;
    }

    public Mat4 toQuaternion(Vec4 q) {
        q.w = (float) Math.sqrt(1.0f + m00 + m11 + m22) / 2.0f;
        q.x = (m21 - m12) / (4.0f * q.w);
        q.y = (m02 - m20) / (4.0f * q.w);
        q.z = (m10 - m01) / (4.0f * q.w);
        return this;
    }

    public Mat4 rotateAxis(float ax, float ay, float az, float a) {
        final float A2 = a * 0.5f;
        final float sinA2 = sin(A2);
        final float cosA2 = cos(A2);
        final float qx = ax * sinA2;
        final float qy = ay * sinA2;
        final float qz = az * sinA2;
        final float qw = cosA2;
        rotateQuaternion(qx, qy, qz, qw);
        return this;
    }

    public Mat4 rotateZ(float a) {
//        public float m00, m01, m02, m03;
//        public float m10, m11, m12, m13;
//        public float m20, m21, m22, m23;
//        public float m30, m31, m32, m33;
        tmp.identity();
        tmp.m00 = cos(a);
        tmp.m01 = -sin(a);
        tmp.m10 = sin(a);
        tmp.m11 = cos(a);
        dot(tmp, this, this);
        return this;
    }

    public Mat4 scale(float x, float y, float z) {
        tmp.identity();
        tmp.m00 = x;
        tmp.m11 = y;
        tmp.m22 = z;
        dot(tmp, this, this);
        return this;
    }

    public Mat4 toMatrix(Matrix matrix) {
//        public float m00, m01, m02, m03;
//        public float m10, m11, m12, m13;
//        public float m20, m21, m22, m23;
//        public float m30, m31, m32, m33;
        tmpF[0] = m00;
        tmpF[1] = m01;
        tmpF[2] = m03;
        tmpF[3] = m10;
        tmpF[4] = m11;
        tmpF[5] = m13;
        tmpF[6] = m30;
        tmpF[7] = m31;
        tmpF[8] = m33;
        matrix.setValues(tmpF);
        return this;
    }

    public Mat4 mapRect(RectF dst, RectF src) {
        tmpVec4.x = src.left;
        tmpVec4.y = src.top;
        tmpVec4.z = 0.0f;
        tmpVec4.w = 1.0f;
        tmpVec4.dot(this);

        dst.left = tmpVec4.x / tmpVec4.w;
        dst.top = tmpVec4.y / tmpVec4.w;

        tmpVec4.x = src.right;
        tmpVec4.y = src.bottom;
        tmpVec4.z = 0.0f;
        tmpVec4.w = 1.0f;
        tmpVec4.dot(this);

        dst.right = tmpVec4.x / tmpVec4.w;
        dst.bottom = tmpVec4.y / tmpVec4.w;

        return this;
    }

    public float determinant() {
        return m00 * coFactor(0, 0) +
                m10 * coFactor(0, 1) +
                m20 * coFactor(0, 2) +
                m30 * coFactor(0, 3);
    }

    private float determinant3(
            float m00, float m01, float m02,
            float m10, float m11, float m12,
            float m20, float m21, float m22) {
        return
                m00 * coFactor3(0, 0,
                        m00, m01, m02,
                        m10, m11, m12,
                        m20, m21, m22)
                        +
                        m10 * coFactor3(0, 1,
                                m00, m01, m02,
                                m10, m11, m12,
                                m20, m21, m22)
                        +
                        m20 * coFactor3(0, 2,
                                m00, m01, m02,
                                m10, m11, m12,
                                m20, m21, m22);
    }

    private float coFactor(int c, int r) {
        float det = 0.0f;

//        public float m00, m01, m02, m03;
//        public float m10, m11, m12, m13;
//        public float m20, m21, m22, m23;
//        public float m30, m31, m32, m33;

        switch (c) {
            case 0:
                switch (r) {
                    case 0:
                        det = determinant3(
                                m11, m12, m13,
                                m21, m22, m23,
                                m31, m32, m33);
                        break;
                    case 1:
                        det = determinant3(
                                m01, m02, m03,
                                m21, m22, m23,
                                m31, m32, m33);
                        break;
                    case 2:
                        det = determinant3(
                                m01, m02, m03,
                                m11, m12, m13,
                                m31, m32, m33);
                        break;
                    case 3:
                        det = determinant3(
                                m01, m02, m03,
                                m11, m12, m13,
                                m21, m22, m23);
                        break;
                }
                break;
            case 1:
                switch (r) {
                    case 0:
                        det = determinant3(
                                m10, m12, m13,
                                m20, m22, m23,
                                m30, m32, m33);
                        break;
                    case 1:
                        det = determinant3(
                                m00, m02, m03,
                                m20, m22, m23,
                                m30, m32, m33);
                        break;
                    case 2:
                        det = determinant3(
                                m00, m02, m03,
                                m10, m12, m13,
                                m30, m32, m33);
                        break;
                    case 3:
                        det = determinant3(
                                m00, m02, m03,
                                m10, m12, m13,
                                m20, m22, m23);
                        break;
                }
                break;
            case 2:
                switch (r) {
                    case 0:
                        det = determinant3(
                                m10, m11, m13,
                                m20, m21, m23,
                                m30, m31, m33);
                        break;
                    case 1:
                        det = determinant3(
                                m00, m01, m03,
                                m20, m21, m23,
                                m30, m31, m33);
                        break;
                    case 2:
                        det = determinant3(
                                m00, m01, m03,
                                m10, m11, m13,
                                m30, m31, m33);
                        break;
                    case 3:
                        det = determinant3(
                                m00, m01, m03,
                                m10, m11, m13,
                                m20, m21, m23);
                        break;
                }
                break;
            case 3:
                switch (r) {
                    case 0:
                        det = determinant3(
                                m10, m11, m12,
                                m20, m21, m22,
                                m30, m31, m32);
                        break;
                    case 1:
                        det = determinant3(
                                m00, m01, m02,
                                m20, m21, m22,
                                m30, m31, m32);
                        break;
                    case 2:
                        det = determinant3(
                                m00, m01, m02,
                                m10, m11, m12,
                                m30, m31, m32);
                        break;
                    case 3:
                        det = determinant3(
                                m00, m01, m02,
                                m10, m11, m12,
                                m20, m21, m22);
                        break;
                }
                break;
        }

        if ((r + c) % 2 == 0)
            return det;
        return -det;
    }

    private float coFactor3(int c, int r,
                            float m00, float m01, float m02,
                            float m10, float m11, float m12,
                            float m20, float m21, float m22) {
        switch (c) {
            case 0:
                switch (r) {
                    case 0:
                        return m11 * m22 - m21 * m12;
                    case 1:
                        return m21 * m02 - m01 * m22;
                    case 2:
                        return m01 * m12 - m11 * m02;
                }
            case 1:
                switch (r) {
                    case 0:
                        return m20 * m12 - m10 * m22;
                    case 1:
                        return m00 * m22 - m20 * m02;
                    case 2:
                        return m10 * m02 - m00 * m12;
                }
            case 2:
                switch (r) {
                    case 0:
                        return m10 * m21 - m20 * m11;
                    case 1:
                        return m20 * m01 - m00 * m21;
                    case 2:
                        return m00 * m11 - m10 * m01;
                }
        }
        return 0;
    }

    public Mat4 sub(Mat4 m1) {
        m00 -= m1.m00;
        m01 -= m1.m01;
        m02 -= m1.m02;
        m03 -= m1.m03;

        m10 -= m1.m10;
        m11 -= m1.m11;
        m12 -= m1.m12;
        m13 -= m1.m13;

        m20 -= m1.m20;
        m21 -= m1.m21;
        m22 -= m1.m22;
        m23 -= m1.m23;

        m30 -= m1.m30;
        m31 -= m1.m31;
        m32 -= m1.m32;
        m33 -= m1.m33;
        return this;
    }

    public Mat4 interpolate(Mat4 m1, Mat4 m2, float t) {
        m00 = (m2.m00 - m1.m00) * t + m1.m00;
        m01 = (m2.m01 - m1.m01) * t + m1.m01;
        m02 = (m2.m02 - m1.m02) * t + m1.m02;
        m03 = (m2.m03 - m1.m03) * t + m1.m03;

        m10 = (m2.m10 - m1.m10) * t + m1.m10;
        m11 = (m2.m11 - m1.m11) * t + m1.m11;
        m12 = (m2.m12 - m1.m12) * t + m1.m12;
        m13 = (m2.m13 - m1.m13) * t + m1.m13;

        m20 = (m2.m20 - m1.m20) * t + m1.m20;
        m21 = (m2.m21 - m1.m21) * t + m1.m21;
        m22 = (m2.m22 - m1.m22) * t + m1.m22;
        m23 = (m2.m23 - m1.m23) * t + m1.m23;

        m30 = (m2.m30 - m1.m30) * t + m1.m30;
        m31 = (m2.m31 - m1.m31) * t + m1.m31;
        m32 = (m2.m32 - m1.m32) * t + m1.m32;
        m33 = (m2.m33 - m1.m33) * t + m1.m33;
        return this;
    }

    public Mat4 transpose() {
//        public float m00, m01, m02, m03;
//        public float m10, m11, m12, m13;
//        public float m20, m21, m22, m23;
//        public float m30, m31, m32, m33;
        float t;
        t = m01;
        m01 = m10;
        m10 = t;
        t = m02;
        m02 = m20;
        m20 = t;
        t = m03;
        m03 = m30;
        m30 = t;
        t = m21;
        m21 = m12;
        m12 = t;
        t = m31;
        m31 = m13;
        m13 = t;
        t = m32;
        m32 = m23;
        m23 = t;
        return this;
    }

    public Mat4 invert() throws AssertionError {
        float cof00 = coFactor(0, 0);
        float cof01 = coFactor(0, 1);
        float cof02 = coFactor(0, 2);
        float cof03 = coFactor(0, 3);
        float det =
                m00 * cof00 +
                        m10 * cof01 +
                        m20 * cof02 +
                        m30 * cof03;
        if (Math.abs(det) < 0.001f)
            throw new AssertionError("Determinant Of 0");
        float f = 1.0f / det;
        return set(
                f * cof00, f * cof01, f * cof02, f * cof03,
                f * coFactor(1, 0), f * coFactor(1, 1), f * coFactor(1, 2), f * coFactor(1, 3),
                f * coFactor(2, 0), f * coFactor(2, 1), f * coFactor(2, 2), f * coFactor(2, 3),
                f * coFactor(3, 0), f * coFactor(3, 1), f * coFactor(3, 2), f * coFactor(3, 3)
        );
    }

    /**
     * The 1-norm of this matrix (the maximum column sum)
     *
     * @return the norm
     */
    public float norm1() {
//        public float m00, m01, m02, m03;
//        public float m10, m11, m12, m13;
//        public float m20, m21, m22, m23;
//        public float m30, m31, m32, m33;
        float norm0 = Math.abs(m00) + Math.abs(m10) + Math.abs(m20) + Math.abs(m30);
        float norm1 = Math.abs(m01) + Math.abs(m11) + Math.abs(m21) + Math.abs(m31);
        float norm2 = Math.abs(m02) + Math.abs(m12) + Math.abs(m22) + Math.abs(m32);
        float norm3 = Math.abs(m03) + Math.abs(m13) + Math.abs(m23) + Math.abs(m33);
        return Math.max(norm0, Math.max(norm1, Math.max(norm2, norm3)));
    }

    /**
     * https://github.com/aoe3/Graphics-Projects/blob/master/AdvancedRayTracer/src/egl/math/Matrix3.java
     * <p>
     * Compute polar decomposition of this.
     * Algorithm from:
     * N. Higham, Computing the Polar Decomposition---with Applications
     * SIAM J. Sci. Stat. Comput. 7:4 (Oct 1986)
     */
    public static void polarDecomp(Mat4 src, Mat4 outQ, Mat4 outP) {
        final float TOL = 1e-6f;
        Mat4 Xprev = new Mat4();
        Mat4 X = new Mat4(src);
        Mat4 Y = new Mat4();
        do {
            Y.set(X).invert();
            Xprev.set(X);
            X.interpolate(X, Y.transpose(), 0.5f);
        } while (new Mat4(X).sub(Xprev).norm1() > TOL * Xprev.norm1());
        outQ.set(X);
        outP.set(X).transpose();
        dot(outP, src, outP);
    }

    private static final Vec4 tmpVec4 = new Vec4();
    private static final Mat4 tmpMat4 = new Mat4();

    public static void decomp(Mat4 src, Mat4 outQ, Mat4 outP) {

        outQ.identity();
        {
            outQ.m00 = src.m00;
            outQ.m01 = src.m01;
            outQ.m02 = src.m02;

            outQ.m10 = src.m10;
            outQ.m11 = src.m11;
            outQ.m12 = src.m12;

            outQ.m20 = src.m20;
            outQ.m21 = src.m21;
            outQ.m22 = src.m22;

            tmpVec4.x = src.m03;
            tmpVec4.y = src.m13;
            tmpVec4.z = src.m23;
            tmpVec4.w = 1.0f;

            tmpMat4.set(outQ);
            tmpMat4.invert();

            tmpVec4.dot(tmpMat4);
        }

        outP.identity();
        {
            outP.m03 = tmpVec4.x;
            outP.m13 = tmpVec4.y;
            outP.m23 = tmpVec4.z;
        }
    }

    public static Mat4 lookAt(Vec4 eye, Vec4 center, Vec4 up) {
        Vec4 f = new Vec4();
        Vec4 u = new Vec4();
        Vec4 s = new Vec4();

        Vec4.substract(center, eye, f);
        f.normalize3();

        u.set(up);
        u.normalize3();

        Vec4.cross(f, u, s);
        s.normalize3();

        Vec4.cross(s, f, u);

        Mat4 m = new Mat4(1.0f);
        m.m00 = s.x;
        m.m01 = s.y;
        m.m02 = s.z;
        m.m10 = u.x;
        m.m11 = u.y;
        m.m12 = u.z;
        m.m20 = -f.x;
        m.m21 = -f.y;
        m.m22 = -f.z;
        m.m03 = -Vec4.dot(s, eye);
        m.m13 = -Vec4.dot(u, eye);
        m.m23 = Vec4.dot(f, eye);

        return m;
    }

    public static Mat4 perspectiveFov(float fovy, float aspect, float zNear, float zFar) {

        float range = tan(fovy * 0.5f) * zNear;
        float left = -range * aspect;
        float right = range * aspect;
        float bottom = -range;
        float top = range;

        Mat4 m = new Mat4(0.0f);
        m.m00 = (2.0f * zNear) / (right - left);
        m.m11 = (2.0f * zNear) / (top - bottom);
        m.m22 = -(zFar + zNear) / (zFar - zNear);
        m.m32 = -1.0f;
        m.m23 = (-2.0f * zFar * zNear) / (zFar - zNear);
        return m;
    }

    public static Mat4 perspective(float w, float h, float zNear, float zFar) {

        float left = -w * 0.5f;
        float right = w * 0.5f;
        float bottom = -h * 0.5f;
        float top = h * 0.5f;

        Mat4 m = new Mat4(0.0f);
        m.m00 = (2.0f * zNear) / (right - left);
        m.m11 = (2.0f * zNear) / (top - bottom);
        m.m22 = -(zFar + zNear) / (zFar - zNear);
        m.m32 = -1.0f;
        m.m23 = (-2.0f * zFar * zNear) / (zFar - zNear);
        return m;
    }

    @Override
    public String toString() {
        return "Mat4 m = new Mat4().set(\n" +
                m00 + "f, " +
                "\t" + m01 + "f, " +
                "\t" + m02 + "f, " +
                "\t" + m03 + "f,\n" +
                m10 + "f, " +
                "\t" + m11 + "f, " +
                "\t" + m12 + "f, " +
                "\t" + m13 + "f,\n" +
                m20 + "f, " +
                "\t" + m21 + "f, " +
                "\t" + m22 + "f, " +
                "\t" + m23 + "f,\n" +
                m30 + "f, " +
                "\t" + m31 + "f, " +
                "\t" + m32 + "f, " +
                "\t" + m33 + "f);";
    }
}
