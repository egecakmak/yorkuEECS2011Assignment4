package A4Q2;

import java.util.*;

/**
 * Represents courses and prerequisites. A binary search tree sorted by course
 * number is used for fast access. This links out to a directed graph
 * representing course dependencies (prerequisites). The graph uses a hash table
 * to represent incoming and outgoing edges. Incoming edges represent courses
 * that are prerequisites for a course. Outgoing edges represent courses for
 * which the course is a prerequisite.
 * 
 * @author jameselder
 */
public class Courses {

	private TreeMap<CourseNumber, Course> courseMap; // for fast access
	private AdjacencyMapGraph<CourseNumber, CourseRequisite> courseGraph;// to represent dependencies

	public Courses() {
		courseMap = new TreeMap<>();
		courseGraph = new AdjacencyMapGraph<>(true); // Directed graph
	}

	// Returns course associated with course number
	public Course getCourse(CourseNumber courseNum) {
		return courseMap.get(courseNum);
	}

	// Returns edge representing dependency of course2 on course 1, null if no
	// dependency exists.
	public Edge<CourseRequisite> getRequisite(CourseNumber course1, CourseNumber course2) {
		return courseGraph.getEdge(courseMap.get(course1).getCourseVertex(), courseMap.get(course2).getCourseVertex());
	}

	// Adds course to database. If course number already exists, updates course
	// name.
	public Course putCourse(CourseNumber courseNum, String courseName) {
		Course course = courseMap.get(courseNum);
		if (course == null) { // add course
			Vertex<CourseNumber> vertex = courseGraph.insertVertex(courseNum); // add to graph
			course = new Course(courseNum, courseName, vertex);
			courseMap.put(courseNum, course); // add to map
		} else {
			course.setCourseName(courseName); // course exists - update name
		}
		return course;
	}

	// Make course1 a requisite for course2
	public Edge<CourseRequisite> putRequisite(CourseNumber courseNum1, CourseNumber courseNum2,
			CourseRequisite requisite) throws InvalidCourseNumberException, CircularPreRequisiteException {
		Course course1 = courseMap.get(courseNum1);
		Course course2 = courseMap.get(courseNum2);
		Edge<CourseRequisite> edgeToReturn = null;
		if (course1 != null && course2 != null) { // If course1 and course2 exists
			Vertex<CourseNumber> course1Vertex = course1.getCourseVertex();
			Vertex<CourseNumber> course2Vertex = course2.getCourseVertex();
			courseGraph.insertEdge(course1Vertex, course2Vertex, requisite);
			edgeToReturn = courseGraph.getEdge(course1Vertex, course2Vertex);
			Map<Vertex<CourseNumber>, Edge<CourseRequisite>> forest = new ProbeHashMap<>();
			Set<Vertex<CourseNumber>> known = new HashSet<Vertex<CourseNumber>>();
			Set<Edge<CourseRequisite>> backEdges = new HashSet<>();
			DFS(courseGraph, course1Vertex, known, forest, backEdges);
			if (backEdges.size() > 0) {
				throw new CircularPreRequisiteException();
			}
		}

		return edgeToReturn;
	}

	/**
	 * Performs depth-first search of the unknown portion of Graph g starting at
	 * Vertex u.
	 *
	 * @param g
	 *            Graph instance
	 * @param u
	 *            Vertex of graph g that will be the source of the search
	 * @param known
	 *            is a set of previously discovered vertices
	 * @param forest
	 *            is a map from nonroot vertex to its discovery edge in DFS forest
	 *
	 *            As an outcome, this method adds newly discovered vertices
	 *            (including u) to the known set, and adds discovery graph edges to
	 *            the forest.
	 */
	public static <V, E> void DFS(Graph<V, E> g, Vertex<V> u, Set<Vertex<V>> known, Map<Vertex<V>, Edge<E>> forest,
			Set<Edge<E>> backEdges) {
		known.add(u); // u has been discovered
		DefaultComparator<V> comp = new DefaultComparator<>();
		for (Edge<E> e : g.outgoingEdges(u)) { // for every outgoing edge from u
			Vertex<V> v = g.opposite(u, e);
			if (!known.contains(v)) {
				DFS(g, v, known, forest, backEdges); // recursively explore from v
				forest.put(v, e); // e is the tree edge that discovered v
			} else if (forest.get(v) == null) {
				backEdges.add(e);
			}
		}
	}

	/**
	 * Returns a string representation of the courses.
	 */
	public String toString() {
		Iterable<Entry<CourseNumber, Course>> courses = courseMap.entrySet();
		String courseMapEntries = new String("Courses: \n");
		for (Entry<CourseNumber, Course> course : courses) {
			courseMapEntries = courseMapEntries + course.getValue().toString() + "\n";
		}
		return (courseMapEntries + courseGraph.toString() + "\n");
	}
}