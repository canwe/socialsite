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

package com.socialsite.persistence;

/**
 * 
 * Activation Domain Model
 * 
 * @author Ananth
 * 
 */
public class Activation implements AbstractDomain
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private Admin admin;
	private String universityName;

	public Activation()
	{
	}

	public Admin getAdmin()
	{
		return admin;
	}

	public long getId()
	{
		return id;
	}

	public String getUniversityName()
	{
		return universityName;
	}

	public void setAdmin(final Admin admin)
	{
		this.admin = admin;
	}

	public void setId(final long id)
	{
		this.id = id;
	}

	public void setUniversityName(final String universityName)
	{
		this.universityName = universityName;
	}

}
