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

package com.socialsite.university;

import com.socialsite.BasePage;
import com.socialsite.course.CoursesPanel;
import com.socialsite.dataprovider.UniversityCourseDataProvider;
import com.socialsite.persistence.University;
import com.socialsite.staff.StaffsPanel;

/**
 * 
 * university main page
 * 
 * @author Ananth
 */
public class UniversityPage extends BasePage
{

	/**
	 * constructor
	 */
	public UniversityPage(final University university)
	{
		add(new UniversityInfoPanel("info", university));
		add(new CoursesPanel("courses", new UniversityCourseDataProvider(university.getId())));
		add(new StaffsPanel("staffs", university));
	}

}
