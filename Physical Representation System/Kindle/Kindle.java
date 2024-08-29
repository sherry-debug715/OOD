package Physical Representation System.Kindle;

import java.util.ArrayList;
import java.util.List;

public class Kindle {
	private Map<String, Book> library;
	EBookReaderFactory bookFactory;
	public Kindle() {
		library = new HashMap<>(); // key: name, value: Book
		bookFactory = new EBookReaderFactory();
	}

	public String readBook(Book book) {
		EBookReader bookReader = bookFactory.createReader(book);
		return bookReader.displayReaderType() + ", book content is: " + reader.readBook();;
	}

	public void downloadBook(Book book) throws BookNotFoundException {
		String bookName = book.file.getName();
		if (!library.containsKey(bookName)) {
			throw new BookNotFoundException(book);
		}
		library.get(bookName).getBook();
	}

	public void uploadBook(Book book) throws UploadedBookAlreadyExistException, FileFormatException {
		String bookName = book.file.getName();
		String bookFormatType = book.file.getFormat();
		if (library.containsKey(bookName)) {
			throw new UploadedBookAlreadyExistException(book);
		}
		if (!bookFormatType.equals("PDF") && !bookFormatType.equals("MOBI") && !bookFormatType.equals("EPUB")) {
			throw new FileFormatException(book);
		}
		library.put(bookName, book);
	}

	public void deleteBook(Book book) {
		String bookName = book.getBook().getName();
		library.remove(bookName);
	}
}

enum BookFormat {
	PDF,
	MOBI,
	EPUB
}

class File {
	private String name;
	private BookFormat format;
	private String content;
	public File(String _name, BookFormat _format, String _content) {
		name = _name;
		format = _format;
		content = _content;
	}
	
	public String getName() {
		return name;
	}
	
	public BookFormat getFormat() {
		return format;
	}

	public String getContent() {
		return content;
	}
}

class Book {
	File file;
	public Book(File _file) {
		file = _file;
	}

	public File getBook() {
		return file;
	}
}

abstract class EBookReader {
	
	protected Book book;
	
	public EBookReader(Book _book){
		book = _book;
	}
	
	public abstract String readBook();
	public abstract String displayReaderType();
}

class EBookReaderFactory {

	public EBookReader createReader(Book book) {
        switch (book.getFile().getFormat()) {
            case PDF:
                return new PDFReader(book);
            case MOBI:
                return new MOBIReader(book);
            case EPUB:
                return new EPUBReader(book);
            default:
                return null; // or throw an UnsupportedFormatException
        }
	}
}

class EPUBReader extends EBookReader{

	public EPUBReader(Book book) {
		super(book);
	}

	@Override
	public String readBook() {
		return book.file.getContent();
	}

	@Override
	public String displayReaderType() {
		return "Using EPUB reader";
	}
}

class MOBIReader extends EBookReader {

	public MOBIReader(Book book) {
		super(book);
	}

	@Override
	public String readBook() {
		return book.file.getContent();
	}

	@Override
	public String displayReaderType() {
		return "Using MOBI reader";
	}

}
class PDFReader extends EBookReader{

	public PDFReader(Book book) {
		super(book);
	}

	@Override
	public String readBook() {
		return book.file.getContent();
	}

	@Override
	public String displayReaderType() {
		return "Using PDF reader";
	}
}

abstract class KindleException extends Exception {
	Book book;
	public KindleException(Book _book) {
		book = _book;
	}

	public abstract String displayErrorMessage();
}

class BookNotFoundException extends KindleException {
	public BookNotFoundException(Book book) {
		super(book);
	}

	@Override
	public String displayErrorMessage() {
		return "The book " + book.getBook().getName() + " is not found";
	}
}

class UploadedBookAlreadyExistException extends KindleException {
	public UploadedBookAlreadyExistException(Book book) {
		super(book);
	}

	@Override
	public String displayErrorMessage() {
		return "The book " + book.getBook().getName() + " already exist";
	}
}

class FileFormatException extends KindleException {
	public FileFormatException(Book book) {
		super(book);
	}

	@Override
	public String displayErrorMessage() {
		return "Your book format " + book.getBook().getFormat() + " doesn't meet our requirement";
	}
}
