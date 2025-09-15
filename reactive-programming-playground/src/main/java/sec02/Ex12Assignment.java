package sec02;

import common.Util;
import sec02.assignment.FileServiceImpl;

public class Ex12Assignment {

    public static void main(String[] args) {
        var fileService = new FileServiceImpl();

        fileService.write("test-01.txt", "Hello world").subscribe(Util.subscriber());
        fileService.read("test-01.txt").subscribe(Util.subscriber());
        fileService.delete("test-01.txt").subscribe(Util.subscriber());
    }
}
