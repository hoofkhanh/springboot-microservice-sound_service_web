package com.hokhanh.web.conversation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hokhanh.web.artist.ArtistClient;
import com.hokhanh.web.common.ArtistCustomerResponse;
import com.hokhanh.web.customer.CustomerClient;
import com.hokhanh.web.exception.ConversationException;
import com.hokhanh.web.user.Role;

import feign.FeignException.FeignClientException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConversationService {

	private final ConversationRepository repo;
	private final ConversationMapper mapper;
	
	private final LatestConversationRepository latestConversationRepository;
	private final LatestConversationMapper latestConversationMapper;
	
	private final ArtistClient artistClient;
	private final CustomerClient customerClient;

	@Transactional
	public ConversationResponse addMessage( ConversationRequest request) {
		ArtistCustomerResponse sender = null;
		String senderId = request.senderIdRole().split(" ")[0];
		if(request.senderIdRole().split(" ")[1].equals(Role.CUSTOMER.toString())) {
			try {
				sender = customerClient.findById(senderId);
			} catch (FeignClientException e) {
				throw new ConversationException("Not found Conversation (sender) customer with this id: "+ senderId);
			}
		}else {
			try {
				sender= artistClient.findById(senderId);
			} catch (FeignClientException e) {
				throw new ConversationException("Not found Conversation (sender) artist with this id: "+ senderId);
			}
		}
		
		ArtistCustomerResponse receiver = null;
		String receiverId = request.receiverIdRole().split(" ")[0];
		if(request.receiverIdRole().split(" ")[1].equals(Role.CUSTOMER.toString())) {
			try {
				receiver = customerClient.findById(receiverId);
			} catch (FeignClientException e) {
				throw new ConversationException("Not found Conversation (receiver) customer with this id: "+ receiverId);
			}
		}else {
			try {
				receiver= artistClient.findById(receiverId);
			} catch (FeignClientException e) {
				throw new ConversationException("Not found Conversation (receiver) artist with this id: "+ receiverId);
			}
		}
		
		var conversation = repo.save(mapper.toConversation(request));
		
		if(request.oldId() != null) {
			var latestConversations = latestConversationRepository.findByConversationId(request.oldId());
			LocalDateTime currentDateTime = LocalDateTime.now();
			for (LatestConversation latestConversation : latestConversations) {
				latestConversation.setConversationId(conversation.getId());
				latestConversation.setDateTime(currentDateTime);
				latestConversationRepository.save(latestConversation);
			}
		}else {
			LocalDateTime currentDateTime = LocalDateTime.now();
			latestConversationRepository.save(latestConversationMapper.toLatestConversation(request.senderIdRole(), 
					conversation.getId(), currentDateTime));
			latestConversationRepository.save(latestConversationMapper.toLatestConversation(request.receiverIdRole(), 
					conversation.getId(), currentDateTime));
		}
		
		return mapper.toConversationResponse(conversation, sender, receiver);
	}

	public LatestConvetsationResponseList findLatestConversationByAccountOwnerIdAndRole(String accountOwnerIdRole, int page) {
		var latestConversations = latestConversationRepository
				.findByAccountOwnerIdRole(accountOwnerIdRole, PageRequest.of(page, 4, Sort.by("dateTime").descending()));
		var total = latestConversations.getTotalElements();
		
		var latestConversationResponses = latestConversations
				.stream()
				.map(latestConversation -> {
					ArtistCustomerResponse accountOwner = null;
					String accountOwnerId = latestConversation.getAccountOwnerIdRole().split(" ")[0];
					if(latestConversation.getAccountOwnerIdRole().split(" ")[1].equals(Role.CUSTOMER.toString())) {
						try {
							accountOwner = customerClient.findById(accountOwnerId);
						} catch (FeignClientException e) {
							throw new ConversationException("Not found Conversation (accountOwner) customer with this id: "+ accountOwnerId);
						}
					}else {
						try {
							accountOwner= artistClient.findById(accountOwnerId);
						} catch (FeignClientException e) {
							throw new ConversationException("Not found Conversation (accountOwner) artist with this id: "+ accountOwnerId);
						}
					}
					
					var conversation = repo.findById(latestConversation.getConversationId())
						    .orElseThrow(() -> new ConversationException("Not found Conversation with this ID: " 
						    		+ latestConversation.getConversationId()));
					
					ArtistCustomerResponse sender = null;
					String senderId = conversation.getSenderIdRole().split(" ")[0];
					if(conversation.getSenderIdRole().split(" ")[1].equals(Role.CUSTOMER.toString())) {
						try {
							sender = customerClient.findById(senderId);
						} catch (FeignClientException e) {
							throw new ConversationException("Not found Conversation (sender) customer with this id: "+ senderId);
						}
					}else {
						try {
							sender= artistClient.findById(senderId);
						} catch (FeignClientException e) {
							throw new ConversationException("Not found Conversation (sender) artist with this id: "+ senderId);
						}
					}
					
					ArtistCustomerResponse receiver = null;
					String receiverId = conversation.getReceiverIdRole().split(" ")[0];
					if(conversation.getReceiverIdRole().split(" ")[1].equals(Role.CUSTOMER.toString())) {
						try {
							receiver = customerClient.findById(receiverId);
						} catch (FeignClientException e) {
							throw new ConversationException("Not found Conversation (receiver) customer with this id: "+ receiverId);
						}
					}else {
						try {
							receiver= artistClient.findById(receiverId);
						} catch (FeignClientException e) {
							throw new ConversationException("Not found Conversation (receiver) artist with this id: "+ receiverId);
						}
					}
					
					var conversationResponse = mapper.toConversationResponse(conversation, sender, receiver);

					
					return latestConversationMapper.toLatestConversationResponse(latestConversation, accountOwner , conversationResponse );
				})
				.toList();
		
		return new LatestConvetsationResponseList(latestConversationResponses, total);
	}

	public ConversationResponseList findConversationOfTwoPeople(String person1IdRole, String person2IdRole,
			int page) {
		var conversations = repo.findConversations(person1IdRole, person2IdRole, person2IdRole, person1IdRole,
				PageRequest.of(page, 10, Sort.by("createdDate").descending()));
		
		var total = conversations.getTotalElements();
		
		var conversationResponses = conversations
				.stream()
				.map(conversation -> {
					ArtistCustomerResponse sender = null;
					String senderId = conversation.getSenderIdRole().split(" ")[0];
					if(conversation.getSenderIdRole().split(" ")[1].equals(Role.CUSTOMER.toString())) {
						try {
							sender = customerClient.findById(senderId);
						} catch (FeignClientException e) {
							throw new ConversationException("Not found Conversation (sender) customer with this id: "+ senderId);
						}
					}else {
						try {
							sender= artistClient.findById(senderId);
						} catch (FeignClientException e) {
							throw new ConversationException("Not found Conversation (sender) artist with this id: "+ senderId);
						}
					}
					
					ArtistCustomerResponse receiver = null;
					String receiverId = conversation.getReceiverIdRole().split(" ")[0];
					if(conversation.getReceiverIdRole().split(" ")[1].equals(Role.CUSTOMER.toString())) {
						try {
							receiver = customerClient.findById(receiverId);
						} catch (FeignClientException e) {
							throw new ConversationException("Not found Conversation (receiver) customer with this id: "+ receiverId);
						}
					}else {
						try {
							receiver= artistClient.findById(receiverId);
						} catch (FeignClientException e) {
							throw new ConversationException("Not found Conversation (receiver) artist with this id: "+ receiverId);
						}
					}
					
					return mapper.toConversationResponse(conversation, sender, receiver);
				})
				.collect(Collectors.toList());
		
		Collections.reverse(conversationResponses);
		
		return new ConversationResponseList(conversationResponses, total);
	}

	public LatestConversationResponse findLatestConversationByAccountOwnerIdRoleAndConversationId(String accountOwnerIdRole,
			String conversationId) {
		var latestConversation = latestConversationRepository.findByAccountOwnerIdRoleAndConversationId(accountOwnerIdRole, conversationId);
		
		ArtistCustomerResponse accountOwner = null;
		String accountOwnerId = latestConversation.getAccountOwnerIdRole().split(" ")[0];
		if(latestConversation.getAccountOwnerIdRole().split(" ")[1].equals(Role.CUSTOMER.toString())) {
			try {
				accountOwner = customerClient.findById(accountOwnerId);
			} catch (FeignClientException e) {
				throw new ConversationException("Not found Conversation (accountOwner) customer with this id: "+ accountOwnerId);
			}
		}else {
			try {
				accountOwner= artistClient.findById(accountOwnerId);
			} catch (FeignClientException e) {
				throw new ConversationException("Not found Conversation (accountOwner) artist with this id: "+ accountOwnerId);
			}
		}
		
		var conversation = repo.findById(latestConversation.getConversationId())
			    .orElseThrow(() -> new ConversationException("Not found Conversation with this ID: " 
			    		+ latestConversation.getConversationId()));
		
		ArtistCustomerResponse sender = null;
		String senderId = conversation.getSenderIdRole().split(" ")[0];
		if(conversation.getSenderIdRole().split(" ")[1].equals(Role.CUSTOMER.toString())) {
			try {
				sender = customerClient.findById(senderId);
			} catch (FeignClientException e) {
				throw new ConversationException("Not found Conversation (sender) customer with this id: "+ senderId);
			}
		}else {
			try {
				sender= artistClient.findById(senderId);
			} catch (FeignClientException e) {
				throw new ConversationException("Not found Conversation (sender) artist with this id: "+ senderId);
			}
		}
		
		ArtistCustomerResponse receiver = null;
		String receiverId = conversation.getReceiverIdRole().split(" ")[0];
		if(conversation.getReceiverIdRole().split(" ")[1].equals(Role.CUSTOMER.toString())) {
			try {
				receiver = customerClient.findById(receiverId);
			} catch (FeignClientException e) {
				throw new ConversationException("Not found Conversation (receiver) customer with this id: "+ receiverId);
			}
		}else {
			try {
				receiver= artistClient.findById(receiverId);
			} catch (FeignClientException e) {
				throw new ConversationException("Not found Conversation (receiver) artist with this id: "+ receiverId);
			}
		}
		
		var conversationResponse = mapper.toConversationResponse(conversation, sender, receiver);

		
		return latestConversationMapper.toLatestConversationResponse(latestConversation, accountOwner , conversationResponse );
	}

	public long checkNumberOfMessageUnread(String accountOwnerIdRole) {
		return latestConversationRepository.findByAccountOwnerIdRole(accountOwnerIdRole)
				.stream()
				.map(latestConversation -> {
					var conversation = repo.findById(latestConversation.getConversationId())
						    .orElseThrow(() -> new ConversationException("Not found Conversation with this ID: " 
						    		+ latestConversation.getConversationId()));
					return conversation;
				})
				.filter(conversation -> conversation.getReceiverIdRole().equals(accountOwnerIdRole) && !conversation.isRead())
				.count();
	}

	public void readMessage(String senderIdRole, String receiverIdRole) {
		var list = repo.findBySenderIdRoleAndReceiverIdRoleAndIsReadFalse(senderIdRole, receiverIdRole);
		for (Conversation conversation : list) {
			if(!conversation.isRead()) {
				conversation.setRead(true);
				repo.save(conversation);
			}
		}
	}

	public  List<LatestConversationResponse> findLatestConversationByName(String accountOwnerIdRole, String name) {
		var listReturn = new ArrayList<LatestConversation>();
		
		var list = latestConversationRepository.findByAccountOwnerIdRole(accountOwnerIdRole);
		for (LatestConversation latestConversation : list) {
			var conversation = repo.findById(latestConversation.getConversationId())
				    .orElseThrow(() -> new ConversationException("Not found Conversation with this ID: " 
				    		+ latestConversation.getConversationId()));
			
			if(conversation.getReceiverIdRole().equals(accountOwnerIdRole)) {
				ArtistCustomerResponse sender = null;
				String senderId = conversation.getSenderIdRole().split(" ")[0];
				if(conversation.getSenderIdRole().split(" ")[1].equals(Role.CUSTOMER.toString())) {
					try {
						sender = customerClient.findById(senderId);
					} catch (FeignClientException e) {
						throw new ConversationException("Not found Conversation (sender) customer with this id: "+ senderId);
					}
				}else {
					try {
						sender= artistClient.findById(senderId);
					} catch (FeignClientException e) {
						throw new ConversationException("Not found Conversation (sender) artist with this id: "+ senderId);
					}
				}
				
				if(sender != null && sender.user().role().toString().equals(Role.ARTIST.toString()) 
						&& sender.artistName().toLowerCase().contains(name.toLowerCase()) ) {
					listReturn.add(latestConversation);
				}else if ((sender != null && sender.user().role().toString().equals(Role.CUSTOMER.toString()) 
						&& sender.user().fullName().toLowerCase().contains(name.toLowerCase()) )) {
					listReturn.add(latestConversation);
				}
			}else {
				ArtistCustomerResponse receiver = null;
				String receiverId = conversation.getReceiverIdRole().split(" ")[0];
				if(conversation.getReceiverIdRole().split(" ")[1].equals(Role.CUSTOMER.toString())) {
					try {
						receiver = customerClient.findById(receiverId);
					} catch (FeignClientException e) {
						throw new ConversationException("Not found Conversation (receiver) customer with this id: "+ receiverId);
					}
				}else {
					try {
						receiver= artistClient.findById(receiverId);
					} catch (FeignClientException e) {
						throw new ConversationException("Not found Conversation (receiver) artist with this id: "+ receiverId);
					}
				}
				
				if(receiver != null && receiver.user().role().toString().equals(Role.ARTIST.toString()) 
						&& receiver.artistName().toLowerCase().contains(name.toLowerCase()) ) {
					listReturn.add(latestConversation);
				}else if ((receiver != null && receiver.user().role().toString().equals(Role.CUSTOMER.toString()) 
						&& receiver.user().fullName().toLowerCase().contains(name.toLowerCase()) )) {
					listReturn.add(latestConversation);
				}
			}
		}
		
		return listReturn.stream()
		.map(latestConversation -> {
			ArtistCustomerResponse accountOwner = null;
			String accountOwnerId = latestConversation.getAccountOwnerIdRole().split(" ")[0];
			if(latestConversation.getAccountOwnerIdRole().split(" ")[1].equals(Role.CUSTOMER.toString())) {
				try {
					accountOwner = customerClient.findById(accountOwnerId);
				} catch (FeignClientException e) {
					throw new ConversationException("Not found Conversation (accountOwner) customer with this id: "+ accountOwnerId);
				}
			}else {
				try {
					accountOwner= artistClient.findById(accountOwnerId);
				} catch (FeignClientException e) {
					throw new ConversationException("Not found Conversation (accountOwner) artist with this id: "+ accountOwnerId);
				}
			}
			
			var conversation = repo.findById(latestConversation.getConversationId())
				    .orElseThrow(() -> new ConversationException("Not found Conversation with this ID: " 
				    		+ latestConversation.getConversationId()));
			
			ArtistCustomerResponse sender = null;
			String senderId = conversation.getSenderIdRole().split(" ")[0];
			if(conversation.getSenderIdRole().split(" ")[1].equals(Role.CUSTOMER.toString())) {
				try {
					sender = customerClient.findById(senderId);
				} catch (FeignClientException e) {
					throw new ConversationException("Not found Conversation (sender) customer with this id: "+ senderId);
				}
			}else {
				try {
					sender= artistClient.findById(senderId);
				} catch (FeignClientException e) {
					throw new ConversationException("Not found Conversation (sender) artist with this id: "+ senderId);
				}
			}
			
			ArtistCustomerResponse receiver = null;
			String receiverId = conversation.getReceiverIdRole().split(" ")[0];
			if(conversation.getReceiverIdRole().split(" ")[1].equals(Role.CUSTOMER.toString())) {
				try {
					receiver = customerClient.findById(receiverId);
				} catch (FeignClientException e) {
					throw new ConversationException("Not found Conversation (receiver) customer with this id: "+ receiverId);
				}
			}else {
				try {
					receiver= artistClient.findById(receiverId);
				} catch (FeignClientException e) {
					throw new ConversationException("Not found Conversation (receiver) artist with this id: "+ receiverId);
				}
			}
			
			var conversationResponse = mapper.toConversationResponse(conversation, sender, receiver);

			
			return latestConversationMapper.toLatestConversationResponse(latestConversation, accountOwner , conversationResponse );
		})
		.toList();
	}




}
