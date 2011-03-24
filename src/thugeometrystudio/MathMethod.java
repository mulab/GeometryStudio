
package thugeometrystudio;


public class MathMethod
{
    public static double getPowLength(MyPoint mp1, MyPoint mp2)
    {
        return (double)(Math.pow(mp1.mypoint.x-mp2.mypoint.x, 2)+Math.pow(mp1.mypoint.y-mp2.mypoint.y, 2));
    }

    public static double getLength(MyPoint mp1, MyPoint mp2)
    {
        return Math.sqrt(getPowLength(mp1,mp2));
    }

    public static double getLength(Line l)
    {
        return getLength(l.start,l.to);
    }

    public static double getLength(Circle c)
    {
        return 2*Math.PI*c.radius;
    }

    public static double getAngle(MyPoint mp1, MyPoint mp2, MyPoint mp3)
    {
        double a2 = getPowLength(mp1,mp2);
        double b2 = getPowLength(mp2,mp3);
        double c2 = getPowLength(mp1,mp3);
        double a = getLength(mp1,mp2);
        double b = getLength(mp2,mp3);
        return Math.acos((a2+b2-c2)/(2*a*b))*180/Math.PI;
    }
}
