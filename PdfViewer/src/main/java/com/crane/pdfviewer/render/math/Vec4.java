package com.crane.pdfviewer.render.math;

public class  Vec4 {
    public float x, y, z, w;

    private static Vec4 tmp = new Vec4();

    public Vec4(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = 1.0f;
    }

    public Vec4(Vec4 v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = v.w;
    }

    public Vec4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vec4() {
        this.w = 1.0f;
    }

    public void dot(Mat4 m) {
//        public float m00, m01, m02, m03;
//        public float m10, m11, m12, m13;
//        public float m20, m21, m22, m23;
//        public float m30, m31, m32, m33;
        tmp.x = x * m.m00 + y * m.m01 + z * m.m02 + w * m.m03;
        tmp.y = x * m.m10 + y * m.m11 + z * m.m12 + w * m.m13;
        tmp.z = x * m.m20 + y * m.m21 + z * m.m22 + w * m.m23;
        tmp.w = x * m.m30 + y * m.m31 + z * m.m32 + w * m.m33;
        set(tmp);
    }

    public static float dot(Vec4 v1, Vec4 v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }

    public static void cross(Vec4 v1, Vec4 v2, Vec4 res) {
        res.x = v1.y * v2.z - v2.y * v1.z;
        res.y = v1.z * v2.x - v2.z * v1.x;
        res.z = v1.x * v2.y - v2.x * v1.y;
    }

    public static void add(Vec4 v1, Vec4 v2, Vec4 res) {
        res.x = v1.x + v2.x;
        res.y = v1.y + v2.y;
        res.z = v1.z + v2.z;
        res.w = v1.w + v2.w;
    }

    public static void substract(Vec4 v1, Vec4 v2, Vec4 res) {
        res.x = v1.x - v2.x;
        res.y = v1.y - v2.y;
        res.z = v1.z - v2.z;
        res.w = v1.w - v2.w;
    }

    public void set(Vec4 src) {
        x = src.x;
        y = src.y;
        z = src.z;
        w = src.w;
    }

    public void toQuaternion() {
        final float A2 = w * 0.5f;
        final float sinA2 = sin(A2);
        final float cosA2 = cos(A2);
        final float qx = x * sinA2;
        final float qy = y * sinA2;
        final float qz = z * sinA2;
        final float qw = cosA2;
        x = qx;
        y = qy;
        z = qz;
        w = qw;
    }

    private static float sin(float a) {
        return (float) Math.sin(a);
    }

    private static float cos(float a) {
        return (float) Math.cos(a);
    }

    public void mul(float a) {
        x *= a;
        y *= a;
        z *= a;
        w *= a;
    }

    void translate(float dx, float dy, float dz) {
        x += dx;
        y += dy;
        z += dz;
    }

    void scale(float sx, float sy, float sz) {
        x *= sx;
        y *= sy;
        z *= sz;
    }

    public float len3() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public float len4() {
        return (float) Math.sqrt(x * x + y * y + z * z + w * w);
    }

    public void normalize3() {
        float l = len3();
        x /= l;
        y /= l;
        z /= l;
    }

    public void normalize4() {
        float l = len4();
        x /= l;
        y /= l;
        z /= l;
        w /= l;
    }

    void rotate(float ax, float ay, float az) {

        float x_res;
        float y_res;
        float z_res;

        // rotate x
        float sin_x = sin(ax);
        float cos_x = cos(ax);

        y_res = z * sin_x + y * cos_x;
        z_res = z * cos_x - y * sin_x;

        y = y_res;
        z = z_res;

        // rotate z
        float sin_z = sin(az);
        float cos_z = cos(az);

        x_res = x * cos_z - y * sin_z;
        y_res = x * sin_z + y * cos_z;

        x = x_res;
        y = y_res;

        // rotate y
        float sin_y = sin(ay);
        float cos_y = cos(ay);

        x_res = x * cos_y - z * sin_y;
        z_res = x * sin_y + z * cos_y;

        x = x_res;
        z = z_res;
    }

    public static void slerp(Vec4 qa, Vec4 qb, Vec4 qm, float t) {
        // Calculate angle between them.
        double cosHalfTheta = qa.w * qb.w + qa.x * qb.x + qa.y * qb.y + qa.z * qb.z;
        // if qa=qb or qa=-qb then theta = 0 and we can return qa
        if (Math.abs(cosHalfTheta) >= 1.0) {
            qm.w = qa.w;
            qm.x = qa.x;
            qm.y = qa.y;
            qm.z = qa.z;
            return;
        }
        // Calculate temporary values.
        float halfTheta = (float) Math.acos(cosHalfTheta);
        float sinHalfTheta = (float) Math.sqrt(1.0 - cosHalfTheta * cosHalfTheta);
        // if theta = 180 degrees then result is not fully defined
        // we could rotate around any axis normal to qa or qb
        if (Math.abs(sinHalfTheta) < 0.001) {
            qm.w = (qa.w * 0.5f + qb.w * 0.5f);
            qm.x = (qa.x * 0.5f + qb.x * 0.5f);
            qm.y = (qa.y * 0.5f + qb.y * 0.5f);
            qm.z = (qa.z * 0.5f + qb.z * 0.5f);
            return;
        }
        float ratioA = sin((1.0f - t) * halfTheta) / sinHalfTheta;
        float ratioB = sin(t * halfTheta) / sinHalfTheta;
        //calculate Quaternion.
        qm.w = (qa.w * ratioA + qb.w * ratioB);
        qm.x = (qa.x * ratioA + qb.x * ratioB);
        qm.y = (qa.y * ratioA + qb.y * ratioB);
        qm.z = (qa.z * ratioA + qb.z * ratioB);
    }
}
