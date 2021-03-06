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

package com.socialsite.authentication;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.hibernate.exception.ConstraintViolationException;

import com.socialsite.SocialSiteSession;
import com.socialsite.activation.UniversityActivator;
import com.socialsite.dao.ProfileDao;
import com.socialsite.dao.UserDao;
import com.socialsite.home.HomePage;
import com.socialsite.image.DefaultImage;
import com.socialsite.persistence.Admin;
import com.socialsite.persistence.Profile;
import com.socialsite.persistence.Staff;
import com.socialsite.persistence.Student;
import com.socialsite.persistence.User;
import com.socialsite.user.UserType;

/**
 * 
 * TODO send a confirmation message to the email
 * 
 * sign up form
 * 
 * @author Ananth
 */
public class SignUpPage extends WebPage
{
	/** Model object for username */
	private String userName;
	/** Model object for password */
	private String password;
	/** Model object for password */
	private String rePassword;

	/** Model object for email */
	private String email;

	/** Model object for university name */
	private String universityName;

	/** Model for the userType **/
	private UserType userType;

	/** Feedback panel */
	private final FeedbackPanel feedback;

	/** Spring Dao to handle profile object */
	@SpringBean(name = "profileDao")
	private ProfileDao profileDao;

	/** Spring Dao to handle user object */
	@SpringBean(name = "userDao")
	private UserDao<User> userDao;

	public SignUpPage()
	{

		// sign up form
		final Form<Object> form = new Form<Object>("signupform");
		add(form);

		final TextField<String> username = new RequiredTextField<String>("username",
				new PropertyModel<String>(this, "userName"));
		username.add(StringValidator.maximumLength(16));

		form.add(username);

		final TextField<String> emailTextField = new RequiredTextField<String>("email",
				new PropertyModel<String>(this, "email"));
		emailTextField.add(EmailAddressValidator.getInstance());

		form.add(emailTextField);

		final PasswordTextField passwordTextField = new PasswordTextField("password",
				new PropertyModel<String>(this, "password"));
		passwordTextField.setRequired(true);
		passwordTextField.add(StringValidator.lengthBetween(6, 16));

		form.add(passwordTextField);

		final PasswordTextField rePasswordTextField = new PasswordTextField("re-password",
				new PropertyModel<String>(this, "rePassword"));
		rePasswordTextField.setRequired(true);
		rePasswordTextField.add(StringValidator.lengthBetween(6, 16));

		form.add(rePasswordTextField);

		final RadioGroup<UserType> userGroup = new RadioGroup<UserType>("usertype",
				new PropertyModel<UserType>(this, "userType"));

		userGroup.add(new Radio<UserType>("student", new Model<UserType>(UserType.STUDENT)));
		userGroup.add(new Radio<UserType>("staff", new Model<UserType>(UserType.STAFF)));
		userGroup.add(new Radio<UserType>("admin", new Model<UserType>(UserType.ADMIN)));
		form.add(userGroup);

		form.add(new TextField<String>("university", new PropertyModel<String>(this,
				"universityName")));

		SubmitLink signUp;
		form.add(signUp = new SubmitLink("signup")
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit()
			{

				if (!password.equals(rePassword))
				{
					error("passwords should be same");
					return;
				}

				// creates a user
				try
				{
					User user = null;

					switch (userType)
					{
						case STUDENT :
							user = new Student(userName, password);
							break;
						case STAFF :
							user = new Staff(userName, password);
							break;
						case ADMIN :
							if (universityName != null && !universityName.equals(""))
							{
								user = new Admin(userName, password);
								userDao.save(user);
								new UniversityActivator((Admin)user, universityName).create();
								// final InfoMsg message = new InfoMsg();
								// message.getUsers().add(user);
								// message.setMessage("Your request for adding new University is being processed");
								// messageDao.save(message);
							}
							else
							{
								error("University Name required");
								return;
							}

							break;
						default :
							error("Unknown error occured ");
							return;
					}

					userDao.save(user);

					final Profile p = new Profile();
					p.setUser(user);
					user.setProfile(p);
					p.setEmail(email);
					// set the default image for the profile
					new DefaultImage().forUser(p);
					profileDao.save(p);

					final SocialSiteSession session = SocialSiteSession.get();
					session
							.setSessionUser(new SessionUser(user.getId(), SocialSiteRoles.ownerRole));
					session.setUserId(user.getId());
					setResponsePage(new HomePage());

				}
				catch (final ConstraintViolationException ex)
				{
					// updates the feedback panel
					error("username already exists");
					// target.addComponent(feedback);
					return;
				}
			}
		});
		// submit the form on return key
		form.setDefaultButton(signUp);

		// feedback panel
		feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		add(feedback);
	}
}
