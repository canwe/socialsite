/**
 *     Copyright SocialSite (C) 2009
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.socialsite.dataprovider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.socialsite.dao.CourseDao;
import com.socialsite.dao.UniversityDao;
import com.socialsite.entitymodel.EntityModel;
import com.socialsite.persistence.Course;
import com.socialsite.persistence.University;

/**
 * TODO: write separate routines to get the course list and size in the course
 * dao
 * 
 * @author Ananth
 * 
 */
public class CoursesDataProvider extends SortableDataProvider<Course>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** used to get the courses */
	@SpringBean(name = "universityDao")
	private UniversityDao universityDao;

	@SpringBean(name = "courseDao")
	private CourseDao courseDao;
	/** university */
	private final University university;

	public CoursesDataProvider(final University university)
	{
		InjectorHolder.getInjector().inject(this);
		// reloads the university from DB.Avoids LIE
		this.university = universityDao.load(university.getId());
	}


	public Iterator<? extends Course> iterator(final int first, final int count)
	{

		final List<Course> courses = new ArrayList<Course>(university.getCourses());
		return courses.subList(first, first + count).iterator();
	}

	public IModel<Course> model(final Course course)
	{
		return new EntityModel<Course>(course, courseDao);
	}

	public int size()
	{
		return university.getCourses().size();
	}

}
