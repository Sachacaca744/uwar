/*
 * Decompiled with CFR 0.152.
 */
package fr.uwar.client;

import fr.uwar.Kind;
import fr.uwar.client.Mesh;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Models {
    private static final Map<Kind, Model> CACHE = new EnumMap<Kind, Model>(Kind.class);
    public static Mesh bomb = new Mesh();
    public static Mesh missile = new Mesh();
    public static Mesh rocket = new Mesh();

    public static synchronized Model get(Kind k) {
        return CACHE.computeIfAbsent(k, kk -> kk.heli() ? Models.heli() : Models.jet());
    }

    static void buildBomb(Mesh b) {
        b.revolve(9, 'z', 0.0f, 0.0f, 0.0f, new float[][]{{-0.45f, 0.03f}, {-0.3f, 0.09f}, {0.0f, 0.125f}, {0.25f, 0.11f}, {0.42f, 0.06f}, {0.48f, 0.0f}}, 12);
        b.revolve(5, 'z', 0.0f, 0.0f, 0.12f, new float[][]{{0.0f, 0.126f}, {0.05f, 0.126f}}, 12);
        for (int i = 0; i < 4; ++i) {
            b.push().rotZ(i * 90 + 45).box(1, 0.0f, 0.12f, -0.38f, 0.008f, 0.07f, 0.09f).pop();
        }
    }

    static void buildMissile(Mesh m) {
        m.revolve(2, 'z', 0.0f, 0.0f, 0.0f, new float[][]{{-0.65f, 0.05f}, {-0.55f, 0.07f}, {0.3f, 0.07f}, {0.5f, 0.05f}, {0.65f, 0.0f}}, 10);
        m.revolve(5, 'z', 0.0f, 0.0f, 0.1f, new float[][]{{0.0f, 0.071f}, {0.06f, 0.071f}}, 10);
        for (int i = 0; i < 4; ++i) {
            m.push().rotZ(i * 90).box(9, 0.0f, 0.1f, -0.56f, 0.005f, 0.06f, 0.07f).pop();
            m.push().rotZ(i * 90 + 45).box(9, 0.0f, 0.07f, 0.15f, 0.004f, 0.04f, 0.06f).pop();
        }
    }

    static void buildRocket(Mesh m) {
        m.revolve(2, 'z', 0.0f, 0.0f, 0.0f, new float[][]{{-0.5f, 0.05f}, {-0.4f, 0.07f}, {0.25f, 0.07f}, {0.42f, 0.04f}, {0.5f, 0.0f}}, 8);
        m.revolve(5, 'z', 0.0f, 0.0f, 0.08f, new float[][]{{0.0f, 0.071f}, {0.05f, 0.071f}}, 8);
        for (int i = 0; i < 4; ++i) {
            m.push().rotZ(i * 90 + 45).box(9, 0.0f, 0.08f, -0.42f, 0.005f, 0.05f, 0.06f).pop();
        }
        m.revolve(15, 'z', 0.0f, 0.0f, -0.75f, new float[][]{{-0.35f, 0.0f}, {-0.1f, 0.05f}, {0.2f, 0.06f}}, 8);
    }

    private static Ord ord(Model m, int weapon, int idx, float x, float y, float z, float scale, int kind) {
        Ord o = new Ord();
        o.weapon = weapon;
        o.idx = idx;
        o.m = new Mesh();
        o.m.push().translate(x, y, z).scale(scale);
        switch (kind) {
            case 0: {
                Models.buildMissile(o.m);
                break;
            }
            case 1: {
                Models.buildBomb(o.m);
                break;
            }
            case 2: {
                o.m.revolve(2, 'z', 0.0f, 0.0f, 0.0f, new float[][]{{-0.5f, 0.2f}, {-0.45f, 0.24f}, {0.45f, 0.24f}, {0.5f, 0.2f}}, 10);
                o.m.revolve(5, 'z', 0.0f, 0.0f, 0.42f, new float[][]{{0.0f, 0.245f}, {0.06f, 0.245f}}, 10);
                break;
            }
        }
        o.m.pop();
        m.ord.add(o);
        return o;
    }

    private static void wheel(Mesh b, float x, float y, float z, float r, float w) {
        b.tube(4, 'x', x, y, z, r, r, w, 10);
        b.tube(2, 'x', x, y, z, r * 0.5f, r * 0.5f, w + 0.02f, 8);
    }

    static Model heli() {
        int s;
        Model m = new Model();
        Mesh b = m.body;
        b.loft(13, new float[][]{{3.9f, 0.95f, 0.1f, 0.1f}, {3.4f, 1.05f, 0.38f, 0.4f}, {2.6f, 1.2f, 0.62f, 0.7f}, {1.4f, 1.35f, 0.78f, 0.88f}, {0.0f, 1.4f, 0.8f, 0.92f}, {-1.4f, 1.45f, 0.7f, 0.8f}, {-2.4f, 1.55f, 0.5f, 0.55f}}, 0.0f, 2.4f, 12);
        b.loft(0, new float[][]{{-2.2f, 1.6f, 0.42f, 0.45f}, {-4.0f, 1.75f, 0.3f, 0.33f}, {-6.0f, 1.95f, 0.18f, 0.2f}, {-6.4f, 2.0f, 0.14f, 0.16f}}, 0.0f, 2.2f, 10);
        b.ellipsoid(3, 0.0f, 1.88f, 2.65f, 0.4f, 0.34f, 0.85f, 12, 8);
        b.ellipsoid(3, 0.0f, 2.18f, 1.15f, 0.44f, 0.4f, 0.9f, 12, 8);
        for (int s2 = -1; s2 <= 1; s2 += 2) {
            b.ellipsoid(1, (float)s2 * 0.95f, 1.98f, -0.5f, 0.38f, 0.36f, 1.2f, 10, 6);
            b.box(2, (float)s2 * 0.95f, 1.98f, 0.75f, 0.28f, 0.26f, 0.04f);
            b.revolve(9, 'z', (float)s2 * 0.95f, 1.98f, -1.85f, new float[][]{{-0.2f, 0.3f}, {0.2f, 0.34f}}, 10);
        }
        b.tube(2, 'y', 0.0f, 2.6f, -0.3f, 0.14f, 0.1f, 0.6f, 8);
        b.box(2, 0.0f, 2.95f, -0.3f, 0.2f, 0.08f, 0.2f);
        b.push().translate(0.0f, 2.4f, -5.9f).rotX(18.0f).box(0, 0.0f, 0.0f, 0.0f, 0.05f, 0.7f, 0.45f).pop();
        b.box(1, 0.0f, 1.95f, -5.5f, 1.1f, 0.04f, 0.3f);
        b.box(14, 0.0f, 3.05f, -6.2f, 0.05f, 0.05f, 0.05f);
        b.box(1, 0.0f, 1.15f, 0.3f, 1.9f, 0.05f, 0.4f);
        b.box(7, 1.92f, 1.15f, 0.3f, 0.05f, 0.06f, 0.06f);
        b.box(14, -1.92f, 1.15f, 0.3f, 0.05f, 0.06f, 0.06f);
        b.box(2, 0.0f, 0.72f, 3.1f, 0.12f, 0.12f, 0.35f);
        b.tube(9, 'z', 0.0f, 0.72f, 3.95f, 0.05f, 0.05f, 1.1f, 6);
        b.ellipsoid(2, 0.0f, 1.05f, 3.75f, 0.16f, 0.16f, 0.22f, 8, 5);
        Mesh g = m.gear;
        for (s = -1; s <= 1; s += 2) {
            g.box(2, (float)s * 0.78f, 0.68f, 0.6f, 0.04f, 0.32f, 0.04f);
            Models.wheel(g, (float)s * 0.8f, 0.3f, 0.6f, 0.3f, 0.2f);
        }
        g.box(2, 0.0f, 1.1f, -4.8f, 0.03f, 0.8f, 0.03f);
        Models.wheel(g, 0.0f, 0.3f, -4.8f, 0.18f, 0.12f);
        m.rx = 0.0f;
        m.ry = 3.05f;
        m.rz = -0.3f;
        m.rotor.blades(4, 5.6f, 0.42f);
        m.rotorDisc.disc(12, 5.6f, 28);
        m.tx = 0.25f;
        m.ty = 2.05f;
        m.tz = -6.35f;
        m.tail.blades(4, 0.95f, 0.16f);
        m.tailDisc.disc(12, 0.95f, 16);
        for (s = -1; s <= 1; s += 2) {
            int i = s < 0 ? 0 : 1;
            Models.ord(m, 1, i, (float)s * 1.1f, 0.88f, 0.4f, 1.8f, 2);
            m.missSt.add(new float[]{(float)s * 1.75f, 0.92f, 0.45f});
            m.missSt.add(new float[]{(float)s * 2.2f, 0.92f, 0.45f});
        }
        m.pivotY = 1.4f;
        return m;
    }

    private static void wing(Mesh b, int mat, float s, float[][] sec) {
        float[][] t = new float[sec.length][];
        for (int i = 0; i < sec.length; ++i) {
            t[i] = new float[]{sec[i][0] * s, sec[i][1], sec[i][2], sec[i][3], sec[i][4]};
        }
        b.foil(mat, t);
    }

    static Model jet() {
        int s;
        Model m = new Model();
        Mesh b = m.body;
        b.loft(13, new float[][]{{6.2f, 1.0f, 0.03f, 0.03f}, {5.4f, 1.0f, 0.18f, 0.17f}, {4.2f, 1.04f, 0.4f, 0.38f}, {2.8f, 1.08f, 0.52f, 0.5f}, {0.8f, 1.1f, 0.6f, 0.56f}, {-1.5f, 1.1f, 0.6f, 0.55f}, {-3.6f, 1.05f, 0.52f, 0.5f}, {-5.2f, 1.0f, 0.44f, 0.42f}, {-5.7f, 1.0f, 0.42f, 0.4f}}, 0.0f, 2.3f, 12);
        b.revolve(2, 'z', 0.0f, 1.0f, 5.8f, new float[][]{{-0.6f, 0.19f}, {0.0f, 0.1f}, {0.45f, 0.0f}}, 10);
        b.ellipsoid(3, 0.0f, 1.58f, 2.2f, 0.36f, 0.34f, 1.05f, 12, 8);
        b.box(1, 0.0f, 0.55f, 2.4f, 0.36f, 0.2f, 0.8f);
        b.box(2, 0.0f, 0.55f, 3.22f, 0.3f, 0.17f, 0.02f);
        float[][] w = new float[][]{{0.5f, 0.98f, -2.4f, 3.8f, 0.3f}, {2.5f, 1.0f, -3.1f, 2.6f, 0.18f}, {4.9f, 1.02f, -3.7f, 1.4f, 0.1f}};
        Models.wing(b, 0, 1.0f, w);
        Models.wing(b, 0, -1.0f, w);
        float[][] tp = new float[][]{{0.5f, 1.0f, -6.0f, 2.6f, 0.18f}, {2.6f, 0.98f, -6.6f, 1.5f, 0.1f}};
        Models.wing(b, 1, 1.0f, tp);
        Models.wing(b, 1, -1.0f, tp);
        b.push().translate(0.0f, 1.0f, 0.0f).rotZ(90.0f);
        b.foil(1, new float[][]{{0.5f, 0.0f, -5.6f, 3.2f, 0.14f}, {2.2f, 0.0f, -6.5f, 1.6f, 0.08f}});
        b.pop();
        b.box(5, 0.0f, 2.8f, -6.3f, 0.09f, 0.18f, 0.2f);
        b.revolve(9, 'z', 0.0f, 1.0f, -6.0f, new float[][]{{-0.5f, 0.4f}, {0.0f, 0.36f}, {0.5f, 0.3f}}, 12);
        b.box(7, 4.95f, 1.02f, -3.0f, 0.04f, 0.05f, 0.1f);
        b.box(14, -4.95f, 1.02f, -3.0f, 0.04f, 0.05f, 0.1f);
        b.box(11, 0.0f, 1.42f, -6.5f, 0.03f, 0.03f, 0.03f);
        m.flame.revolve(15, 'z', 0.0f, 1.0f, -7.8f, new float[][]{{-1.8f, 0.0f}, {-1.2f, 0.2f}, {0.0f, 0.3f}, {1.2f, 0.33f}}, 10);
        Mesh g = m.gear;
        g.box(2, 0.0f, 0.55f, 3.4f, 0.03f, 0.36f, 0.03f);
        Models.wheel(g, 0.0f, 0.2f, 3.4f, 0.17f, 0.14f);
        for (s = -1; s <= 1; s += 2) {
            g.box(2, (float)s * 0.9f, 0.55f, -0.4f, 0.04f, 0.45f, 0.04f);
            Models.wheel(g, (float)s * 0.9f, 0.2f, -0.4f, 0.22f, 0.18f);
        }
        for (s = -1; s <= 1; s += 2) {
            int i = s < 0 ? 0 : 1;
            m.missSt.add(new float[]{(float)s * 5.1f, 1.0f, -2.8f});
            m.missSt.add(new float[]{(float)s * 2.7f, 0.78f, -1.0f});
            m.bombSt.add(new float[]{(float)s * 1.6f, 0.55f, 0.1f});
            m.bombSt.add(new float[]{(float)s * 3.5f, 0.7f, -1.2f});
            Models.ord(m, 1, i, (float)s * 4.2f, 0.82f, -2.0f, 1.8f, 2);
        }
        m.pivotY = 1.0f;
        return m;
    }

    static {
        Models.buildBomb(bomb);
        Models.buildMissile(missile);
        Models.buildRocket(rocket);
    }

    public static class Model {
        public final Mesh body = new Mesh();
        public final Mesh gear = new Mesh();
        public final Mesh flame = new Mesh();
        public final Mesh rotor = new Mesh();
        public final Mesh rotorDisc = new Mesh();
        public final Mesh tail = new Mesh();
        public final Mesh tailDisc = new Mesh();
        public float rx;
        public float ry;
        public float rz;
        public float tx;
        public float ty;
        public float tz;
        public final List<Ord> ord = new ArrayList<Ord>();
        public final List<float[]> missSt = new ArrayList<float[]>();
        public final List<float[]> bombSt = new ArrayList<float[]>();
        public float pivotY = 1.3f;
    }

    public static class Ord {
        public Mesh m;
        public int weapon;
        public int idx;
    }
}

