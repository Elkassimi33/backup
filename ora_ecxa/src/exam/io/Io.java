package exam.io;


import javax.swing.*;
import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.ConsoleHandler;
import java.util.stream.Collectors;

public class Io implements Serializable {
    private  Integer size=1;
    private transient Double d=null;
    private static String name;
     {size=3; name="Green";}

    public Io(){
        size=3; name="Blue";
    }




    public static void main(String[] args) throws IOException {
        var io = new Io();
//        var oldPath = Paths.get("file/test1.txt");
//        var newPath = Paths.get("file/test.txt");
//
//        Files.move(oldPath,newPath, StandardCopyOption.REPLACE_EXISTING);
//
//        try (var bf = new BufferedWriter(new FileWriter(oldPath.toFile(),true)); var s=System.out){
//
//            bf.write("alert!");
//            bf.flush();
//            bf.write("!");
//            s.println("1");
//
//        }catch (IOException e){
//            System.out.println("2");
//        }finally {
//            System.out.println("3");
//        }
//        io.writeResume();

        var p = Path.of("newDir");
//        for (int i=0;i<5;i++){
//            var c=Path.of("dir%duu".formatted(i));
//            if(!Files.exists(p.resolve(c)))
//                Files.createDirectory(p.resolve(c));
//            for (int j = 0; j < 5; j++) {
//                var f=Path.of("file%d.txt".formatted(j));
//                if(!Files.exists(p.resolve(c.resolve(f))))
//                    Files.createFile(p.resolve(c.resolve(f)));
//            }
//        }
//       Files.walk(p,1)
//               .map(q-> {
//                   try {
//                       return q.toRealPath();
//                   } catch (IOException e) {
//                       throw new RuntimeException(e);
//                   }
//               }).forEach(System.out::println);


//        Files.list(p).filter(Files::isRegularFile)
//                        .flatMap(a->{
//                            try {
//                                return Files.lines(a);
//                            } catch (IOException e) {
//                                throw new RuntimeException(e);
//                            }
//                        })
//                                .forEach(System.out::println);


  ;
        Files.readAllLines(Path.of("newDir/dir0uu/file0.txt")).forEach(System.out::println);
    }
}
