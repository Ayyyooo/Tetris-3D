/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package engine;
import engine.elements.Vector;
import engine.elements.Triangle;
import engine.elements.Mesh;
import engine.elements.Vertex;
import java.util.Iterator;
import java.util.LinkedList;
/**
 *
 * @author josja
 */
public class Engine {
    public float [][] projMatrix;
    public Vector cameraVec;
    public Vector lookVec;
    public Vector upVec;
    private Vector toVec;
    private final Vector [][] planeNormals;
    private final float theta;
    private final float near;
    private final float far;
    private float aspect;
    

    
    public Engine(int w, int h, float theta, float near, float far){
        this.projMatrix = new float[4][4];
        this.theta = theta;
        this.near = near;
        this.far = far;
        this.aspect = (float)w/(float)h;
        createProjMat();
        this.cameraVec = new Vector();
        this.lookVec = new Vector(0,0,1);
        this.upVec = new Vector(0,1,0);
        this.toVec = Vector.vectorAdd(cameraVec, lookVec);
        this.planeNormals = new Vector[6][2];
        constructPlanes();
    }
    
    public void changeAspect(int w, int h){
        this.aspect = (float)w/(float)h;
        createProjMat();
        constructPlanes();
    }
    //PROJECTION MATRIX
    //constructs the projection matrix
    private void createProjMat() {
        float f = 1.0f/(float)Math.tan(Math.toRadians(this.theta/2));
        for(float []row: this.projMatrix){
            for(float elem: row) elem = 0.0f;
        }
        this.projMatrix[0][0] = f/aspect;
        this.projMatrix[1][1] = f;
        this.projMatrix[2][2] = far/(far-near);
        this.projMatrix[2][3] = -(far*near)/(far-near);
        this.projMatrix[3][2] = 1;
    }
    
    private void constructPlanes(){
        // 0 normal, 1 point
        //near plane
        float fovX = (float)Math.atan(Math.tan(Math.toRadians(this.theta)/2)*aspect);
        float fovY = (float)Math.toRadians(this.theta/2);
        
        //near plane
        this.planeNormals[0][0] = new Vector(0,0,1);
        this.planeNormals[0][1] = new Vector(0,0,near);
        
        //far plane
        this.planeNormals[1][0] = new Vector(0,0,-1);
        this.planeNormals[1][1] = new Vector(0,0,far);
        
        //bottom plane 
        this.planeNormals[2][0] = new Vector(0,(float)Math.cos(fovY),(float)Math.sin(fovY));
        this.planeNormals[2][1] = new Vector(0,-near * (float)Math.tan(fovY),near); //(0,-1,0)
        
        //top plane
        this.planeNormals[3][0] = new Vector(0,-(float)Math.cos(fovY),(float)Math.sin(fovY));
        this.planeNormals[3][1] = new Vector(0,near * (float)Math.tan(fovY),near); //(0,1,0)
        
        //right plane
        this.planeNormals[4][0] = new Vector(-(float)Math.cos(fovX),0,(float)Math.sin(fovX));
        this.planeNormals[4][1] = new Vector(near * (float)Math.tan(fovX),0,near); //(aspect,0,0)
        
        //left plane
        this.planeNormals[5][0] = new Vector((float)Math.cos(fovX),0,(float)Math.sin(fovX));
        this.planeNormals[5][1] = new Vector(-near * (float)Math.tan(fovX),0,near); //(-aspect,0,0)
    }
    //VIEW TRANSFORMATION MATRIX 
    //creates the look-at matrix  
    public float[][] matrixFrom(){
        toVec = Vector.vectorAdd(cameraVec, lookVec);
        
        Vector forward = Vector.vectorUnit(Vector.vectorSub(toVec, cameraVec));
        
        Vector right = Vector.vectorUnit(Vector.crossProduct(upVec, forward));
        
        Vector up2 = Vector.vectorUnit(Vector.crossProduct(forward, right));
        
        float [][] matrix = new float[4][4];
        
        matrix[0][0] = right.getX();
        matrix[0][1] = right.getY();
        matrix[0][2] = right.getZ();
        matrix[0][3] =  -(Vector.vectorDotP(cameraVec, right));
        
        matrix[1][0] = up2.getX();
        matrix[1][1] = up2.getY();
        matrix[1][2] = up2.getZ();
        matrix[1][3] = -(Vector.vectorDotP(cameraVec, up2));
                
        matrix[2][0] = forward.getX();
        matrix[2][1] = forward.getY();
        matrix[2][2] = forward.getZ();
        matrix[2][3] = -(Vector.vectorDotP(cameraVec, forward));
        
        matrix[3][0] = 0;
        matrix[3][1] = 0;
        matrix[3][2] = 0;
        matrix[3][3] = 1;
        
        
        return matrix;
    }
    //ROTATION MATIX
    //returns a rotation matrix with the given angles
    public static float [][] rotationMatrix(float xAngle, float yAngle, float zAngle){
            float cosX = (float) Math.cos(Math.toRadians(xAngle));
            float sinX = (float) Math.sin(Math.toRadians(xAngle));
            float cosY = (float) Math.cos(Math.toRadians(yAngle));
            float sinY = (float) Math.sin(Math.toRadians(yAngle));
            float cosZ = (float) Math.cos(Math.toRadians(zAngle));
            float sinZ = (float) Math.sin(Math.toRadians(zAngle));
            float[][] matrix = new float[4][4];
            matrix[0][0] = cosY * cosZ;
            matrix[0][1] = (cosZ * sinY* sinX) - (sinZ * cosX) ;
            matrix[0][2] = (sinZ * sinX) + (cosZ * sinY * cosX);
            matrix[0][3] = 0.0f;
            
            matrix[1][0] = cosY * sinZ;
            matrix[1][1] = cosZ * cosX + sinZ * sinY * sinX;
            matrix[1][2] = (sinZ * sinY * cosX)-(cosZ * sinX);
            matrix[1][3] = 0.0f;

            matrix[2][0] = -(sinY);
            matrix[2][1] = sinX * cosY;
            matrix[2][2] = cosX * cosY;
            matrix[2][3] = 0.0f;

            matrix[3][0] = 0;
            matrix[3][1] = 0;
            matrix[3][2] = 0;
            matrix[3][3] = 1.0f;
            return matrix;
    }
    
    public static float[][] translationMatrix(float x, float y, float z){
        float [][] transMatrix = new float[4][4];
            for(float [] row: transMatrix){
                for(float m: row) m=0;
            }
            transMatrix[0][0] = 1;
            transMatrix[1][1] = 1;
            transMatrix[2][2] = 1;
            transMatrix[0][3] = x;
            transMatrix[1][3] = y;
            transMatrix[2][3] = z;
            transMatrix[3][3] = 1;
            return transMatrix;
    }
    
        public static float[][] translationMatrix(Vector vector){
        float [][] transMatrix = new float[4][4];
            for(float [] row: transMatrix){
                for(float m: row) m=0;
            }
            transMatrix[0][0] = 1;
            transMatrix[1][1] = 1;
            transMatrix[2][2] = 1;
            transMatrix[0][3] = vector.getX();
            transMatrix[1][3] = vector.getY();
            transMatrix[2][3] = vector.getZ();
            transMatrix[3][3] = 1;
            return transMatrix;
    }
    
    public static float[][] scalingMatrix(float scaleX, float scaleY, float scaleZ){
        float [][] scaleMatrix = new float[4][4];
            for(float [] row: scaleMatrix){
                for(float m: row) m=0;
            }
            scaleMatrix[0][0] = scaleX;
            scaleMatrix[1][1] = scaleY;
            scaleMatrix[2][2] = scaleZ;
            scaleMatrix[3][3] = 1;

            return scaleMatrix;
    }
    
    //multiplies a point/vector by a matrix
    public static Vector matrixMulti(float [][] matrix, Vector p){
        Vector fCoords = new Vector();
        fCoords.setX(matrix[0][0]*p.getX() + matrix[0][1]*p.getY() + matrix[0][2]*p.getZ() + matrix[0][3]*p.getW());
        fCoords.setY(matrix[1][0]*p.getX() + matrix[1][1]*p.getY() + matrix[1][2]*p.getZ() + matrix[1][3]*p.getW());
        fCoords.setZ(matrix[2][0]*p.getX() + matrix[2][1]*p.getY() + matrix[2][2]*p.getZ() + matrix[2][3]*p.getW());
        fCoords.setW(matrix[3][0]*p.getX() + matrix[3][1]*p.getY() + matrix[3][2]*p.getZ() + matrix[3][3]*p.getW());
        if(fCoords.getW()!=0){
            fCoords.setX(fCoords.getX()/fCoords.getW());
            fCoords.setY(fCoords.getY()/fCoords.getW());
            fCoords.setZ(fCoords.getZ()/fCoords.getW());
        }
        return fCoords;
    }
    
    public static void matrixMultiInPlace(float [][] matrix, Vector p){
            float originalX = p.getX();
            float originalY = p.getY();
            float originalZ = p.getZ();
            float originalW = p.getW();

            p.setX(matrix[0][0]*originalX + matrix[0][1]*originalY + matrix[0][2]*originalZ + matrix[0][3]*originalW);
            p.setY(matrix[1][0]*originalX + matrix[1][1]*originalY + matrix[1][2]*originalZ + matrix[1][3]*originalW);
            p.setZ(matrix[2][0]*originalX + matrix[2][1]*originalY + matrix[2][2]*originalZ + matrix[2][3]*originalW);
            p.setW(matrix[3][0]*originalX + matrix[3][1]*originalY + matrix[3][2]*originalZ + matrix[3][3]*originalW);

            if(p.getW() != 0){
                p.setX(p.getX() / p.getW());
                p.setY(p.getY() / p.getW());
                p.setZ(p.getZ() / p.getW());
            }
    }
    
    //intersection point between a line and a plane
    public static Vertex intersection(Vector [] plane, Vertex start, Vertex end){
        //t=D-<N,A>/<N<B-A>>
        float t = (-Vector.vectorDotP(plane[0], plane[1])-Vector.vectorDotP(start, plane[0])) / (Vector.vectorDotP(plane[0],Vector.vectorSub(end, start)));
        //Q = A+t(B-A)
        Vector intercept = Vector.vectorAdd(start, Vector.vectorMult(Vector.vectorSub(end, start), t));
        //u, v
//        float u = start.getU() + t*(end.getU()-start.getU());
//        float v = start.getV() + t*(end.getV()-start.getV());
        return new Vertex(intercept.getX(),intercept.getY(), intercept.getZ(),0,0);
    }
    //BACK-FACE CULLING
    public void cullBackFaces(Mesh mesh){
        mesh.m.removeIf(tri->{
            Vector normal = Vector.crossProduct(Vector.vectorSub(tri.points[2], tri.points[0]),Vector.vectorSub(tri.points[1], tri.points[0]));
            tri.setNormal(normal);
            return Vector.vectorDotP(normal, tri.points[0])<=0;
        });
    }
    
    /*
    CLIPPING CODE
    accept, reject, or clip triangles, depending on ...
    */
    public void clipTrianglesAgainstPlane(Mesh mesh){
        LinkedList<Triangle> newTris = new LinkedList<>();
        Iterator<Triangle> iterator = mesh.m.iterator();
        while(iterator.hasNext()){
           Triangle tri = iterator.next();
           for(Vector[] plane: this.planeNormals){
                if(clipTriangles(tri, newTris, plane)){
                    iterator.remove();
                    break;
                }
           }
        }
        for (Triangle tri : newTris) {
            mesh.m.add(tri);
        }     
    }
    /*0 normal*/
    private boolean clipTriangles(Triangle tri, LinkedList newTris, Vector[] plane){
            float d0 = (Vector.vectorDotP(plane[0], tri.points[0]) -Vector.vectorDotP(plane[0], plane[1]));
            float d1 = (Vector.vectorDotP(plane[0], tri.points[1]) -Vector.vectorDotP(plane[0], plane[1]));
            float d2 = (Vector.vectorDotP(plane[0], tri.points[2]) -Vector.vectorDotP(plane[0], plane[1]));
            int inside =0;
            int outside=0;
            float [][] insPoint = new float[3][5];
            float [][] outPoint = new float[3][5];
            if(d0>0){
                insPoint[inside][0]= tri.points[0].getX();
                insPoint[inside][1]= tri.points[0].getY();
                insPoint[inside][2]= tri.points[0].getZ();
//                insPoint[inside][3]= tri.points[0].getU();
//                insPoint[inside][4]= tri.points[0].getV();
                inside++;
            }else{
                outPoint[outside][0]= tri.points[0].getX();
                outPoint[outside][1]= tri.points[0].getY();
                outPoint[outside][2]= tri.points[0].getZ();
//                outPoint[outside][3]= tri.points[0].getU();
//                outPoint[outside][4]= tri.points[0].getV();
                outside++;
            }
            
            if(d1>0){
                insPoint[inside][0]= tri.points[1].getX();
                insPoint[inside][1]= tri.points[1].getY();
                insPoint[inside][2]= tri.points[1].getZ(); 
//                insPoint[inside][3]= tri.points[1].getU();
//                insPoint[inside][4]= tri.points[1].getV(); 
                inside++;
            }else{
                outPoint[outside][0]= tri.points[1].getX();
                outPoint[outside][1]= tri.points[1].getY();
                outPoint[outside][2]= tri.points[1].getZ();
//                outPoint[outside][3]= tri.points[1].getU();
//                outPoint[outside][4]= tri.points[1].getV();
                outside++;
            }
            
            if(d2>0){
                insPoint[inside][0]= tri.points[2].getX();
                insPoint[inside][1]= tri.points[2].getY();
                insPoint[inside][2]= tri.points[2].getZ();   
//                insPoint[inside][3]= tri.points[2].getU();
//                insPoint[inside][4]= tri.points[2].getV();   
                inside++;
            }else{
                outPoint[outside][0]= tri.points[2].getX();
                outPoint[outside][1]= tri.points[2].getY();
                outPoint[outside][2]= tri.points[2].getZ();
//                outPoint[outside][3]= tri.points[2].getU();
//                outPoint[outside][4]= tri.points[2].getV();
                outside++;
            }
            
            switch (inside) {
                case 0 -> {
                    newTris.clear();
                    return true;
                }
                case 1 -> {
                    tri.points[0].setX(insPoint[0][0]);
                    tri.points[0].setY(insPoint[0][1]);
                    tri.points[0].setZ(insPoint[0][2]);
                    tri.points[1] = Engine.intersection(plane, new Vertex(insPoint[0][0],insPoint[0][1],insPoint[0][2],insPoint[0][3],insPoint[0][4]), new Vertex(outPoint[0][0],outPoint[0][1],outPoint[0][2],outPoint[0][3],outPoint[0][4]));
                    tri.points[2] = Engine.intersection(plane, new Vertex(insPoint[0][0],insPoint[0][1],insPoint[0][2],insPoint[0][3],insPoint[0][4]), new Vertex(outPoint[1][0],outPoint[1][1],outPoint[1][2],outPoint[1][3],outPoint[1][4]));
                }
                case 2 -> {
                    tri.points[0].setX(insPoint[0][0]);
                    tri.points[0].setY(insPoint[0][1]);
                    tri.points[0].setZ(insPoint[0][2]);
                    tri.points[1].setX(insPoint[1][0]);
                    tri.points[1].setY(insPoint[1][1]);
                    tri.points[1].setZ(insPoint[1][2]);
                    tri.points[2] = Engine.intersection(plane, new Vertex(insPoint[0][0],insPoint[0][1],insPoint[0][2],insPoint[0][3],insPoint[0][4]), new Vertex(outPoint[0][0],outPoint[0][1],outPoint[0][2],outPoint[0][3],outPoint[0][4]));
                    Triangle newTri = new Triangle(new Vertex[] {
                        Engine.intersection(plane, new Vertex(insPoint[0][0],insPoint[0][1],insPoint[0][2],insPoint[0][3],insPoint[0][4]), new Vertex(outPoint[0][0],outPoint[0][1],outPoint[0][2],outPoint[0][3],outPoint[0][4])),
                        new Vertex(insPoint[1][0],insPoint[1][1],insPoint[1][2],0,0),
                        Engine.intersection(plane, new Vertex(insPoint[1][0],insPoint[1][1],insPoint[1][2],insPoint[1][3],insPoint[1][4]), new Vertex(outPoint[0][0],outPoint[0][1],outPoint[0][2],outPoint[0][3],outPoint[0][4])),
                    }); 
                    newTri.setColor(tri.getColor());
                    newTri.setNormal(tri.getNormal());
                    newTris.add(newTri);
                }
                
            }
        return false;
    }
    
}
