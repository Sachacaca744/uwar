/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Matrix4f
 *  org.joml.Matrix4fc
 *  org.joml.Vector3f
 *  org.joml.Vector3fc
 */
package fr.uwar.client;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class Mesh {
    public static final int HA = 0;
    public static final int HB = 1;
    public static final int MET = 2;
    public static final int LENS = 3;
    public static final int RUB = 4;
    public static final int ACC = 5;
    public static final int PROP = 6;
    public static final int LED = 7;
    public static final int SOL = 8;
    public static final int STEEL = 9;
    public static final int CAN = 10;
    public static final int WHITE = 11;
    public static final int BLUR = 12;
    public static final int CAMO = 13;
    public static final int RLED = 14;
    public static final int FLAME = 15;
    public final List<float[]> q = new ArrayList<float[]>();
    private final ArrayDeque<Matrix4f> st = new ArrayDeque();
    private Matrix4f cur = new Matrix4f();
    private static final float[][] FOIL = new float[][]{{1.0f, 0.0f}, {0.7f, 0.55f}, {0.3f, 0.5f}, {0.0f, 0.05f}, {0.3f, -0.3f}, {0.7f, -0.45f}};

    public Mesh push() {
        this.st.push(new Matrix4f((Matrix4fc)this.cur));
        return this;
    }

    public Mesh pop() {
        this.cur = this.st.pop();
        return this;
    }

    public Mesh translate(float x, float y, float z) {
        this.cur.translate(x, y, z);
        return this;
    }

    public Mesh rotX(float d) {
        this.cur.rotateX((float)Math.toRadians(d));
        return this;
    }

    public Mesh rotY(float d) {
        this.cur.rotateY((float)Math.toRadians(d));
        return this;
    }

    public Mesh rotZ(float d) {
        this.cur.rotateZ((float)Math.toRadians(d));
        return this;
    }

    public Mesh scale(float s) {
        this.cur.scale(s);
        return this;
    }

    private Vector3f tv(float x, float y, float z) {
        return this.cur.transformPosition(x, y, z, new Vector3f());
    }

    private void quadW(int mat, Vector3f a, Vector3f b, Vector3f c, Vector3f d, Vector3f hint) {
        Vector3f d2;
        Vector3f d1 = new Vector3f((Vector3fc)c).sub((Vector3fc)a);
        Vector3f n = d1.cross((Vector3fc)(d2 = new Vector3f((Vector3fc)d).sub((Vector3fc)b)), new Vector3f());
        if (n.lengthSquared() < 1.0E-12f) {
            n.set(0.0f, 1.0f, 0.0f);
        } else {
            n.normalize();
        }
        Vector3f cen = new Vector3f((Vector3fc)a).add((Vector3fc)b).add((Vector3fc)c).add((Vector3fc)d).mul(0.25f);
        if (hint != null) {
            Vector3f vector3f = new Vector3f();
            if (n.dot((Vector3fc)cen.sub((Vector3fc)hint, vector3f)) < 0.0f) {
                n.negate();
            }
        }
        int col = mat & 3;
        int row = mat >> 2;
        float u0 = ((float)col + 0.04f) / 4.0f;
        float u1 = ((float)col + 0.96f) / 4.0f;
        float v0 = ((float)row + 0.04f) / 4.0f;
        float v1 = ((float)row + 0.96f) / 4.0f;
        this.q.add(new float[]{a.x, a.y, a.z, b.x, b.y, b.z, c.x, c.y, c.z, d.x, d.y, d.z, u0, v0, u1, v0, u1, v1, u0, v1, n.x, n.y, n.z, mat});
    }

    private static Vector3f avg(Vector3f[] p) {
        Vector3f c = new Vector3f();
        for (Vector3f v : p) {
            c.add((Vector3fc)v);
        }
        return c.mul(1.0f / (float)p.length);
    }

    public Mesh rings(int mat, float[][][] r, boolean capA, boolean capB) {
        int j;
        int i;
        int n = r[0].length;
        Vector3f[][] w = new Vector3f[r.length][n];
        Vector3f[] cen = new Vector3f[r.length];
        for (i = 0; i < r.length; ++i) {
            for (int j2 = 0; j2 < n; ++j2) {
                w[i][j2] = this.tv(r[i][j2][0], r[i][j2][1], r[i][j2][2]);
            }
            cen[i] = Mesh.avg(w[i]);
        }
        for (i = 0; i < r.length - 1; ++i) {
            Vector3f hint = new Vector3f((Vector3fc)cen[i]).add((Vector3fc)cen[i + 1]).mul(0.5f);
            for (int j3 = 0; j3 < n; ++j3) {
                int k = (j3 + 1) % n;
                this.quadW(mat, w[i][j3], w[i][k], w[i + 1][k], w[i + 1][j3], hint);
            }
        }
        if (capA) {
            for (j = 0; j < n; ++j) {
                this.quadW(mat, w[0][j], w[0][(j + 1) % n], cen[0], cen[0], cen[r.length - 1]);
            }
        }
        if (capB) {
            for (j = 0; j < n; ++j) {
                this.quadW(mat, w[r.length - 1][j], w[r.length - 1][(j + 1) % n], cen[r.length - 1], cen[r.length - 1], cen[0]);
            }
        }
        return this;
    }

    private static float sp(double c, float n) {
        return (float)(Math.signum(c) * Math.pow(Math.abs(c), 2.0 / (double)n));
    }

    public static float[][] ell(float z, float cx, float cy, float rx, float ry, float n, int seg) {
        float[][] p = new float[seg][];
        for (int j = 0; j < seg; ++j) {
            double a = Math.PI * 2 * (double)j / (double)seg;
            p[j] = new float[]{cx + rx * Mesh.sp(Math.cos(a), n), cy + ry * Mesh.sp(Math.sin(a), n), z};
        }
        return p;
    }

    public Mesh loft(int mat, float[][] s, float cx, float n, int seg) {
        float[][][] r = new float[s.length][][];
        for (int i = 0; i < s.length; ++i) {
            r[i] = Mesh.ell(s[i][0], cx, s[i][1], s[i][2], s[i][3], n, seg);
        }
        return this.rings(mat, r, true, true);
    }

    public Mesh foil(int mat, float[][] s) {
        float[][][] r = new float[s.length][FOIL.length][];
        for (int i = 0; i < s.length; ++i) {
            for (int j = 0; j < FOIL.length; ++j) {
                r[i][j] = new float[]{s[i][0], s[i][1] + s[i][4] * FOIL[j][1], s[i][2] + s[i][3] * FOIL[j][0]};
            }
        }
        return this.rings(mat, r, false, true);
    }

    public Mesh ellipsoid(int mat, float cx, float cy, float cz, float rx, float ry, float rz, int seg, int stacks) {
        float[][][] r = new float[stacks + 1][seg][];
        for (int i = 0; i <= stacks; ++i) {
            double phi = Math.toRadians(-82.0 + 164.0 * (double)i / (double)stacks);
            for (int j = 0; j < seg; ++j) {
                double th = Math.PI * 2 * (double)j / (double)seg;
                r[i][j] = new float[]{cx + (float)((double)rx * Math.cos(phi) * Math.cos(th)), cy + (float)((double)ry * Math.sin(phi)), cz + (float)((double)rz * Math.cos(phi) * Math.sin(th))};
            }
        }
        return this.rings(mat, r, true, true);
    }

    public Mesh tube(int mat, char axis, float cx, float cy, float cz, float r0, float r1, float len, int seg) {
        this.push();
        this.translate(cx, cy, cz);
        if (axis == 'x') {
            this.rotY(90.0f);
        } else if (axis == 'y') {
            this.rotX(-90.0f);
        }
        float[][][] r = new float[][][]{Mesh.ell(-len / 2.0f, 0.0f, 0.0f, r0, r0, 2.0f, seg), Mesh.ell(len / 2.0f, 0.0f, 0.0f, r1, r1, 2.0f, seg)};
        this.rings(mat, r, true, true);
        this.pop();
        return this;
    }

    public Mesh revolve(int mat, char axis, float cx, float cy, float cz, float[][] zr, int seg) {
        this.push();
        this.translate(cx, cy, cz);
        if (axis == 'x') {
            this.rotY(90.0f);
        } else if (axis == 'y') {
            this.rotX(-90.0f);
        }
        float[][][] r = new float[zr.length][][];
        for (int i = 0; i < zr.length; ++i) {
            r[i] = Mesh.ell(zr[i][0], 0.0f, 0.0f, zr[i][1], zr[i][1], 2.0f, seg);
        }
        this.rings(mat, r, true, true);
        this.pop();
        return this;
    }

    public Mesh box(int mat, float cx, float cy, float cz, float hx, float hy, float hz) {
        float[][] c = new float[8][];
        int k = 0;
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                for (int l = 0; l < 2; ++l) {
                    c[k++] = new float[]{cx + (i == 0 ? -hx : hx), cy + (j == 0 ? -hy : hy), cz + (l == 0 ? -hz : hz)};
                }
            }
        }
        Vector3f[] w = new Vector3f[8];
        for (int i = 0; i < 8; ++i) {
            w[i] = this.tv(c[i][0], c[i][1], c[i][2]);
        }
        Vector3f h = this.tv(cx, cy, cz);
        this.quadW(mat, w[0], w[1], w[3], w[2], h);
        this.quadW(mat, w[4], w[5], w[7], w[6], h);
        this.quadW(mat, w[0], w[1], w[5], w[4], h);
        this.quadW(mat, w[2], w[3], w[7], w[6], h);
        this.quadW(mat, w[0], w[2], w[6], w[4], h);
        this.quadW(mat, w[1], w[3], w[7], w[5], h);
        return this;
    }

    public Mesh disc(int mat, float r, int seg) {
        Vector3f c = this.tv(0.0f, 0.0f, 0.0f);
        Vector3f h = this.tv(0.0f, -1.0f, 0.0f);
        for (int j = 0; j < seg; ++j) {
            double a0 = Math.PI * 2 * (double)j / (double)seg;
            double a1 = Math.PI * 2 * (double)(j + 1) / (double)seg;
            this.quadW(mat, this.tv((float)((double)r * Math.cos(a0)), 0.0f, (float)((double)r * Math.sin(a0))), this.tv((float)((double)r * Math.cos(a1)), 0.0f, (float)((double)r * Math.sin(a1))), c, c, h);
        }
        return this;
    }

    public Mesh blades(int n, float r, float w) {
        for (int i = 0; i < n; ++i) {
            this.push();
            this.rotY((float)i * 360.0f / (float)n);
            this.rotX(10.0f);
            this.box(6, r / 2.0f + 0.01f, 0.0f, 0.0f, r / 2.0f, 0.006f, w / 2.0f);
            this.pop();
        }
        this.box(2, 0.0f, 0.0f, 0.0f, 0.02f, 0.014f, 0.02f);
        return this;
    }
}

